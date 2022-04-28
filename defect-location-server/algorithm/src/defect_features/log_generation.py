# -*- coding: utf-8 -*-
import sys
# sys.path.append("/Users/lifeasarain/Desktop/JITO/JIT-Identification")
from defect_features.config import conf
import os
import subprocess

def wrapper_change_path(func):
    cwd = os.getcwd()

    def inner(*args, **kwargs):
        return func(*args, **kwargs)


    os.chdir(cwd)
    return inner


class GitLog:
    commands = {
        'meta': 'meta_cmd',
        'numstat': 'numstat_cmd',
        'namestat': 'namestat_cmd',
        'merge_numstat': 'merge_numstat_cmd',
        'merge_namestat': 'merge_namestat_cmd'
    }

    def __init__(self):
        self.meta_cmd = 'git log --before="2015-1-4" --reverse --all --pretty=format:\"commit: %H%n' \
                        'parent: %P%n' \
                        'author: %an%n' \
                        'author email: %ae%n' \
                        'time stamp: %at%n' \
                        'committer: %cn%n' \
                        'committer email: %ce%n' \
                        '%B%n\"  '
        self.numstat_cmd = 'git log --before="2015-1-4" --pretty=format:\"commit: %H\" --numstat -M --all --reverse '
        self.namestat_cmd = 'git log --before="2015-1-4" --pretty=format:\"commit: %H\" --name-status -M --all --reverse '
        self.merge_numstat_cmd = 'git log --before="2015-1-4" --pretty=oneline --numstat -m --merges -M --all --reverse '
        self.merge_namestat_cmd = 'git log --before="2015-1-4" --pretty=oneline  --name-status -m --merges -M  --all --reverse '

    @wrapper_change_path
    def run(self, project, end_time):
        target_path = conf.project_path(project)
        print(target_path)
        os.chdir(target_path)

        hcommands = {
            'meta': 'git log'+ ' --before='+end_time+' --reverse --all --pretty=format:\"commit: %H%n' \
                        'parent: %P%n' \
                        'author: %an%n' \
                        'author email: %ae%n' \
                        'time stamp: %at%n' \
                        'committer: %cn%n' \
                        'committer email: %ce%n' \
                        '%B%n\"  ',
            'numstat': 'git log'+ ' --before='+end_time+' --pretty=format:\"commit: %H\" --numstat -M --all --reverse ',
            'namestat': 'git log'+ ' --before='+end_time+' --pretty=format:\"commit: %H\" --name-status -M --all --reverse ',
            'merge_numstat': 'git log'+ ' --before='+end_time+' --pretty=oneline --numstat -m --merges -M --all --reverse ',
            'merge_namestat': 'git log'+ ' --before='+end_time+' --pretty=oneline  --name-status -m --merges -M  --all --reverse '
        }



        for cmd_name in hcommands:
            # print (cmd_name)
            # cmd = getattr(self, GitLog.commands.get(cmd_name))

            cmd = hcommands.get(cmd_name)
            print(cmd)
            log_path = conf.project_log_path(project, cmd_name)
            print(log_path)
            out = subprocess.check_output(cmd,shell=True).decode('utf-8',errors='ignore')
            with open(log_path,'w',encoding='utf-8') as f_obj2:
                f_obj2.write(out)



# if __name__ == '__main__':
#     for p in conf.projects:
#         print('Project',p)
#         GitLog().run(p)
    # for p in conf.projects:
    #     GitLog().commitrun(p, '25ca7fea48b19285a5a482486e1764b59be644fa')

