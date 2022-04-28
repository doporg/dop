#-*- coding: utf-8 -*-
import subprocess
import os
import re
import json
import pdb
from defect_features.config import conf



class GitCommitLinker:
    def __init__(self, project,corrective_commits_dict,all_commits_dict):
        # --diff-flter=M : only consider modified files
        # if "$REPLY" without " ", echo will output the file in current dir
        self.diff_cmd = "git diff {commit_id}^ {commit_id_2} --unified=0 | while read; do echo 'LINE_START:' \"$REPLY\";done"
        #self.diff_cmd = "git diff {commit_id}^ {commit_id_2} --unified=0"
        self.diff_name_cmd = "git diff {commit_id}^ {commit_id_2} --name-only "
        self.blame_cmd = "git blame -L {line},+1 -f -n {commit_id}^ -l -- {file}"
        self.project = project
        # self.project_dir = conf.data_path + "/" + self.project
        self.project_dir = conf.data_path + self.project
        #print("aaaa:"+self.project_dir)
        self.corrective_commits = corrective_commits_dict
        self.all_commits = all_commits_dict

    def is_nosise(self,line):
        #
        line = line.strip('\t').strip('\r').strip()
        # ignore blank line
        if line == '':
            return True
        # ignore comment
        if line.startswith('//') or line.startswith('/**') or line.startswith('*') or \
                line.startswith('/*') or line.endswith('*/'):
            return True
        # ignore import line
        if line.startswith('import ') or line.startswith('package'):
            return True
        return False

    def _get_del_lines(self,diff_raw):
        consider_extentions = conf.consider_extensions
        # files(lines) to be analyzed. key: string, file name; values : list , the line number of deleted line
        del_lines = dict()
        # start to parse git diff information
        # one file one region
        regions = diff_raw.split('LINE_START: diff --git')[1:]
        for region in regions:
            file_name = re.search(r'a/(\S+)',region).group(1)
            # 非目标文件
            file_ext = os.path.splitext(file_name)[1]
            if file_ext.lower() not in consider_extentions:
                continue
            chunks = region.split('LINE_START: @@')[1:]
            for chunk in chunks:
                lines = chunk.split('\n')
                line_info = re.search(r'-(\d*)',lines[0])
                # current line number of file a(usually previous file)
                current_a = int(line_info.group(1))
                # parse each line, except chunk header
                # some file may not delete any line, and we need process this condition later
                for line_raw in lines[1:]:
                    line = line_raw.strip('LINE_START: ')
                    # ignore line not del
                    if not line.startswith('-'):
                        continue
                    # process noise
                    if self.is_nosise(line.strip('-')):
                        # update line num of file a
                        current_a += 1
                        continue
                    if file_name in del_lines:
                        del_lines[file_name].append(str(current_a))
                    else:
                        del_lines[file_name] = [str(current_a)]
                    current_a += 1
        return del_lines

    def get_del_lines(self,commit):
        try:

            diff_raw = subprocess.check_output(self.diff_cmd.format(commit_id=commit.commit_id,commit_id_2=commit.commit_id),shell=True,
                                                  cwd=self.project_dir,executable='/bin/bash').decode('utf-8',errors='ignore')
            del_lines = self._get_del_lines(diff_raw)
            return del_lines
        except Exception as e:
            # print('get_del_lines error:',e)

            return None

    def git_blame(self,del_lines,corrective_commit):
        buggy_commits = []
        if del_lines == {}:
            return None
        corrective_commit.fix_file_num = len(del_lines) # del_lines : dict, key: file_name
        # # 不考虑涉及3个以上文件的commit
        # if corrective_commit.fix_file_num >= 3:
        #     return []
        corrective_commit.bug_fix_files = dict()
        for file, lines in del_lines.items():
            if file not in corrective_commit.bug_fix_files:
                corrective_commit.bug_fix_files[file] = list()
            for line in lines:
                corrective_commit.bug_fix_files[file].append(line)
                try:
                    blame_raw = subprocess.check_output(
                        self.blame_cmd.format(line=line, commit_id=corrective_commit.commit_id, file=file),
                        shell=True,cwd=self.project_dir).decode('utf-8',errors='ignore')
                except Exception as e:
                    continue
                buggy_commit = blame_raw.split()[0]
                buggy_file = blame_raw.split()[1]
                buggy_line = blame_raw.split()[2]
                # strip('^') : ^ is  used for a "boundary commit", usually init commit
                # we think the boundary commit will not be a buggy commit;
                # stackoverflow:
                # https://stackoverflow.com/questions/13105858/why-would-a-lines-sha-in-a-git-blame-view-have-a-leading-caret
                if buggy_commit.startswith('^'):
                    continue
                if buggy_commit not in buggy_commits:
                    buggy_commits.append(buggy_commit)
                    # update interval of finding bug firstly
                    if not self.all_commits[buggy_commit].find_interval:
                        self.all_commits[buggy_commit].find_interval = corrective_commit.time_stamp - \
                                                                       self.all_commits[buggy_commit].time_stamp
                    else:
                        cur_interval = self.all_commits[buggy_commit].find_interval
                        new_interval = corrective_commit.time_stamp - self.all_commits[buggy_commit].time_stamp
                        self.all_commits[buggy_commit].find_interval = cur_interval if cur_interval <= new_interval else new_interval

                if not self.all_commits[buggy_commit].buggy_lines:
                    self.all_commits[buggy_commit].buggy_lines = dict()
                if buggy_file not in self.all_commits[buggy_commit].buggy_lines:
                    self.all_commits[buggy_commit].buggy_lines[buggy_file] = list()
                self.all_commits[buggy_commit].buggy_lines[buggy_file].append(buggy_line)
        # update fixes contains_bug fix_by
        # if corrective_commit delete noise lines or modified files is not targeted, the buggy_commits will be empty
        if buggy_commits != []:
            corrective_commit.fixes = []
        for commit in buggy_commits:
            corrective_commit.fixes.append(commit)
            self.all_commits[commit].contains_bug = True
            if self.all_commits[commit].fix_by == None:
                self.all_commits[commit].fix_by = [corrective_commit.commit_id]
            else:
                self.all_commits[commit].fix_by.append(corrective_commit.commit_id)

    # def _get_add_lines(self, diff_raw):
    #     consider_extentions = conf.consider_extensions
    #     # files(lines) to be analyzed. key: string, file name; values : list , the line number of deleted line
    #     add_lines = dict()
    #     # start to parse git diff information
    #     # one file one region
    #     regions = diff_raw.split('LINE_START: diff --git')[1:]
    #     for region in regions:
    #         file_name = re.search(r' b/(\S+)', region).group(1)
    #         # 非目标文件
    #         file_ext = os.path.splitext(file_name)[1]
    #         if file_ext.lower() not in consider_extentions:
    #             continue
    #         chunks = region.split('LINE_START: @@')[1:]
    #         for chunk in chunks:
    #             lines = chunk.split('\n')
    #             line_info = re.search(r'\+(\d*)', lines[0])
    #             current_num = int(line_info.group(1))
    #             for line_raw in lines[1:]:
    #                 line = line_raw.strip('LINE_START: ')
    #                 # ignore line not del
    #                 if not line.startswith('+'):
    #                     continue
    #                 # process noise
    #                 if self.is_nosise(line.strip('+')):
    #                     # update line num of file a
    #                     current_num += 1
    #                     continue
    #                 if file_name in add_lines:
    #                     add_lines[file_name].append(str(current_num))
    #                 else:
    #                     add_lines[file_name] = [str(current_num)]
    #                 current_num += 1
    #     return add_lines
    def _get_add_lines(self, diff_raw):
        consider_extentions = conf.consider_extensions
        # files(lines) to be analyzed. key: string, file name; values : list , the line number of deleted line
        add_lines = list()
        # start to parse git diff information
        # one file one region
        regions = diff_raw.split('LINE_START: diff --git')[1:]
        for region in regions:
            file_name = re.search(r' b/(\S+)', region).group(1)
            # 非目标文件
            file_ext = os.path.splitext(file_name)[1]
            if file_ext.lower() not in consider_extentions:
                continue
            chunks = region.split('LINE_START: @@')[1:]
            for chunk in chunks:
                lines = chunk.split('\n')
                line_info = re.search(r'\+(\d*)', lines[0])
                current_num = int(line_info.group(1))
                for line_raw in lines[1:]:
                    line = line_raw.strip('LINE_START: ')
                    # ignore line not del
                    if not line.startswith('+'):
                        continue
                    # process noise
                    if self.is_nosise(line.strip('+')):
                        # update line num of file a
                        current_num += 1
                        continue
                    add_lines.append(line.strip('+').lstrip())
                    current_num += 1
        return add_lines


    def get_add_lines(self,commit):
        try:
            # 排除init commit
            diff_raw = subprocess.check_output(self.diff_cmd.format(commit_id=commit.commit_id,commit_id_2=commit.commit_id),shell=True,
                                                  cwd=self.project_dir,executable='/bin/bash').decode('utf-8',errors='ignore')
            add_lines = self._get_add_lines(diff_raw)
            return add_lines
        except Exception as e:
            # print('get_add_lines error:',e)
            return None

    def get_clean_lines(self):
        """
        buggy commit涉及的文件为buggy files，新增的代码行为buggy lines;
        除buggy commit 和 merge commit 以外的commit都视为clean commit,
        clean commit 涉及的文件为clean files，新增的代码行为clean lines.
        但最好去除最近1-3个月的commit。
        :return:
        """
        for commit in self.all_commits.values():
            if commit.contains_bug or commit.classification == "Merge":
                continue
            else:
                commit.clean_lines = self.get_add_lines(commit)

    def _link_corrective_commit(self,corrective_commit):
        del_lines = self.get_del_lines(corrective_commit)
        #print("del_lines wei:  ")
        #print(del_lines)
        if del_lines != {}:
            self.git_blame(del_lines,corrective_commit)

    def link_corrective_commits(self):
        """
        :param corrective_commits: dict
        :return:
        """
        num_corrective = len(self.corrective_commits)
        current = 1
        for id, corrective_commit in self.corrective_commits.items():
            # print(self.project,  'Linking commit（{current}\\{num_corrdctive}):'.format(current=current,
            #               num_corrdctive=num_corrective), corrective_commit.commit_id)
            self._link_corrective_commit(corrective_commit)
            current += 1
        # 获取无缺陷代码行
        self.get_clean_lines()




if __name__ == '__main__':
    linker = GitCommitLinker('pf4j')
    linker.git_blame({ 'pf4j/src/main/java/ro/fortsoft/pf4j/PluginWrapper.java': ['26', '53', '54', '57', '58', '59']}
,'07550657ef0412eae460e57439a8fc543b75233f')
