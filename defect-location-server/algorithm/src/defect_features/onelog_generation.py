# -*- coding: utf-8 -*-
import sys
from defect_features.config import conf
# sys.path.append("/home/qiufc/JITO/Python/JIT_defect_prediction_code")
import os
import subprocess

def wrapper_change_path(func):
    cwd = os.getcwd()

    def inner(*args, **kwargs):
        return func(*args, **kwargs)


    os.chdir(cwd)
    return inner


class GitOneLog:
    commands = {
        'meta': 'meta_cmd',
        'numstat': 'numstat_cmd',
        'namestat': 'namestat_cmd',
        'merge_numstat': 'merge_numstat_cmd',
        'merge_namestat': 'merge_namestat_cmd'
    }
    @wrapper_change_path
    # def commitrun(self, project, commithash):
    #     target_path = conf.project_path(project)
    #     os.chdir(target_path)
    #     hcommands = {
    #         'meta2': 'git show ' + commithash + ' --pretty=format:\"commit: %H%n' \
    #                                            'parent: %P%n' \
    #                                            'author: %an%n' \
    #                                            'author email: %ae%n' \
    #                                            'time stamp: %at%n' \
    #                                            'committer: %cn%n' \
    #                                            'committer email: %ce%n' \
    #                                            '%B%n\"  ',
    #         'numstat2': 'git show ' + commithash + ' --pretty=format:\"commit: %H\" --numstat -M ',
    #         'namestat2': 'git show ' + commithash + ' --pretty=format:\"commit: %H\" --name-status -M ',
    #         'merge_numstat2': 'git show ' + commithash + ' --pretty=oneline --numstat -m --merges -M ',
    #         'merge_namestat2': 'git show ' + commithash + ' --pretty=oneline  --name-status -m --merges -M '
    #     }
    #     for cmd_name in hcommands:
    #         cmd = hcommands.get(cmd_name)
    #         log_path = conf.project_log_path(project, cmd_name)
    #         out = subprocess.check_output(cmd, shell=True).decode('utf-8', errors='ignore')
    #         with open(log_path, 'w') as f_obj2:
    #             f_obj2.write(out)
    def commitrun(self, project):
        target_path = conf.project_path(project)
        os.chdir(target_path)
        hcommands = {
            'meta2': 'git log --reverse --all --pretty=format:\"commit: %H%n' \
                        'parent: %P%n' \
                        'author: %an%n' \
                        'author email: %ae%n' \
                        'time stamp: %at%n' \
                        'committer: %cn%n' \
                        'committer email: %ce%n' \
                        '%B%n\"  ',
            'numstat2': 'git log --pretty=format:\"commit: %H\" --numstat -M --all --reverse ',
            'namestat2': 'git log --pretty=format:\"commit: %H\" --name-status -M --all --reverse ',
            'merge_numstat2': 'git log --pretty=oneline --numstat -m --merges -M --all --reverse ',
            'merge_namestat2': 'git log --pretty=oneline  --name-status -m --merges -M  --all --reverse '
        }
        for cmd_name in hcommands:
            cmd = hcommands.get(cmd_name)
            log_path = conf.project_log_path(project, cmd_name)
            out = subprocess.check_output(cmd, shell=True).decode('utf-8', errors='ignore')
            with open(log_path, 'w',encoding='utf-8') as f_obj2:
                f_obj2.write(out)

# def java_run(commithash):
#     for p in conf.projects:
#         GitOneLog().commitrun(p, commithash)



# if __name__ == '__main__':
#     commithash = "b5f0ec072f3fd0da566e32f82c0e43ca36553f39"
#     java_run(commithash)
    # for p in conf.projects:
    #     print('Project',p)
    #     GitLog().run(p)
    # for p in conf.projects:
    #     GitLog().commitrun(p, '25ca7fea48b19285a5a482486e1764b59be644fa')

