import re
import os
import math
import pdb
from defect_features.utils.extensions import in_our_extensions

normal_modification_pattern = re.compile(r'^[0-9]+\s+[0-9]+\s+\S+', re.MULTILINE)
non_modification_pattern = re.compile(r'-\s+-\s+\S+')
number_pattern = re.compile(r'[0-9]+\t')
merge_modify_pattern = re.compile('[0-9a-f]{40} \(from [0-9a-f]{40}\)')


class RawGitNumStat:
    def __init__(self, is_merge):
        self.commit_id_line = None
        self.file_lines = None
        self.is_merge = is_merge

    def extract_id(self):
        if not self.is_merge:
            return self.commit_id_line[-40:]
        else:
            return self.commit_id_line[0:40]

    def extract_parent_id(self):
        if self.is_merge and merge_modify_pattern.match(self.commit_id_line) is not None:
            return self.commit_id_line[47:87]
        return None

    def extract_file_stats(self):
        if len(self.file_lines) == 0:
            return []
        else:
            file_dicts = list()
            for l in self.file_lines:
                file_dict = dict()
                l = l.lstrip().rstrip()
                numbers = number_pattern.findall(l)
                t_index = l.rindex('\t')
                # 处理重命名文件。
                # 因为entroy涉及代码增删数目，所以从numstat文件获取原始数据。
                if '=>' in l:
                    str1 = l[t_index+1:]
                    index3 = str1.index('=')
                    if '{' in l:
                        index1 = str1.index('{')
                        index2 = str1.index('}')
                        str3 = str1[index1 + 1:index3 - 1]
                    else:
                        index1 = 0
                        index2 = len(l)
                        str3 = str1[index1:index3 - 1]
                    str2 = str1[0:index1]
                    str4 = str1[index3+3:index2]
                    str5 = ''
                    if index2 < len(str1):
                        str5 = str1[index2+1:]
                    file_dict['modified_path'] = (str2 + str3 + str5).replace('//', '/')
                    file_dict['current_path'] = (str2 + str4 + str5).replace('//', '/')
                    file_dict['is_rename'] = True
                    if non_modification_pattern.match(l) is not None:
                        file_dict['added'] = 0
                        file_dict['deleted'] = 0
                    else:
                        file_dict['added'] = int(numbers[0].rstrip('\t'))
                        file_dict['deleted'] = int(numbers[1].rstrip('\t'))
                # 处理modify文件
                elif normal_modification_pattern.match(l) is not None:
                    file_dict['is_rename'] = False
                    file_dict['added'] = int(numbers[0].rstrip())
                    file_dict['deleted'] = int(numbers[1].rstrip())
                    file_dict['modified_path'] = l[t_index+1:]
                # 处理特殊情况
                # 比如，“-	-	pf4j-logo.png”
                else:
                    try:
                        assert(non_modification_pattern.match(l) is not None)
                    except:
                        print (l)
                        raise
                    file_dict['is_rename'] = False
                    file_dict['added'] = 0
                    file_dict['deleted'] = 0
                    file_dict['modified_path'] = l[t_index+1:]
                file_dicts.append(file_dict)
            return file_dicts


class GitNumStat:
    def __init__(self, project):
        self.commit_id = None
        self.project = project
        self.file_stat = None
        self.is_merge = False
        self.base_commit = None
        self.__cache_added_number = None
        self.__cache_deleted_number = None
        self.__cache_modified_files = None
        self.__cache_rename_files = None
        self.__cache_modified_subsystems = None
        self.__cache_modified_dirs = None
        self.__cache_entropy = None

    def from_raw_numstat(self, rgns):
        assert(isinstance(rgns, RawGitNumStat))
        self.commit_id = rgns.extract_id()
        self.file_stat = rgns.extract_file_stats()
        self.is_merge = rgns.is_merge
        self.base_commit = rgns.extract_parent_id()

    @property
    def added_number(self):
        count = 0
        if self.__cache_added_number is None:
            for st in self.file_stat:
                if in_our_extensions(st['modified_path']):
                    count += st['added']
                elif st['is_rename'] and in_our_extensions(st['current_path']):
                    count += st['added']
            self.__cache_added_number = count
        return self.__cache_added_number

    @property
    def deleted_number(self):
        count = 0
        if self.__cache_deleted_number is None:
            for st in self.file_stat:
                if in_our_extensions(st['modified_path']):
                    count += st['deleted']
                elif st['is_rename'] and in_our_extensions(st['current_path']):
                    count += st['deleted']
            self.__cache_deleted_number = count
        return self.__cache_deleted_number

    @property
    def modified_files(self):
        files = set()
        rename_files = dict()
        if self.__cache_modified_files is None:
            for st in self.file_stat:
                files.add((st['modified_path'], st['added'], st['deleted']))
                if st['is_rename']:
                    rename_files[st['modified_path']] = st['current_path']
            self.__cache_modified_files = files
            self.__cache_rename_files = rename_files
        return self.__cache_modified_files, self.__cache_rename_files

    @property
    def modified_subsystems(self):
        subsystem = set()
        if self.__cache_modified_subsystems is None:
            for st in self.file_stat:
                modified_path = st['modified_path']
                try:
                    index1 = modified_path.index('/')
                except ValueError:
                    index1 = 0
                subsystem.add(modified_path[0:index1])
                if st['is_rename']:
                    rename_path = st['current_path']
                    try:
                        index2 = rename_path.index('/')
                    except ValueError:
                        index2 = 0
                    subsystem.add(rename_path[0:index2])
            self.__cache_modified_subsystems = subsystem
        return self.__cache_modified_subsystems

    @property
    def modified_dirs(self):
        directory = set()
        if self.__cache_modified_dirs is None:
            for st in self.file_stat:
                modified_path = st['modified_path']
                directory.add(os.path.dirname(modified_path))
                if st['is_rename']:
                    rename_path = st['current_path']
                    directory.add(os.path.dirname(rename_path))
            self.__cache_modified_dirs = directory
        return self.__cache_modified_dirs

    @property
    def entropy(self):
        entropy = 0
        file_lines = list()
        file_total_line_count = 0
        if self.__cache_entropy is None:
            for st in self.file_stat:
                modified_path = st['modified_path']
                if in_our_extensions(modified_path) or \
                        (st['is_rename'] and in_our_extensions(st['current_path'])):
                    added_loc = st['added']
                    deleted_loc = st['deleted']
                    total_loc = added_loc + deleted_loc
                    file_total_line_count += total_loc
                    file_lines.append(total_loc)
            minimum = 100
            for f_loc in file_lines:
                if f_loc == 0:
                    continue
                else:
                    avg = 1.0 * f_loc / file_total_line_count
                    if avg < minimum:
                       minimum = avg
                    entropy -= avg * math.log(avg, 2)
            if minimum != 100 and math.log(minimum, 2) != 0:
                    entropy /= abs(math.log(minimum, 2))
            self.__cache_entropy = entropy
        return self.__cache_entropy
