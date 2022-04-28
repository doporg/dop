import re

from .analyze_git_logs import commit_id_line_pattern
from defect_features.config import conf
from defect_features.git_analysis.git_stats.git_numstat import GitNumStat
from defect_features.git_analysis.git_stats.git_numstat import RawGitNumStat

merge_commit_id_line_pattern = re.compile('[0-9a-f]{40}')


def load_numstat_lines(project, is_merge):
    if not is_merge:
        log_path = conf.project_log_path(project, 'numstat')
    else:
        log_path = conf.project_log_path(project, 'merge_numstat')
    # f_obj = file(log_path, 'r')
    # log_str = f_obj.read()
    # lines = log_str.split('\n')
    # f_obj.close()
    with open(log_path,'r',encoding='utf-8') as f_obj:
        log_str = f_obj.read()
        lines = log_str.split('\n')
    return lines

# add for one commit
def load_one_numstat_lines(project, is_merge):
    if not is_merge:
        log_path = conf.project_log_path(project, 'numstat2')
    else:
        log_path = conf.project_log_path(project, 'merge_numstat2')
    # f_obj = file(log_path, 'r')
    # log_str = f_obj.read()
    # lines = log_str.split('\n')
    # f_obj.close()
    with open(log_path,'r',encoding='utf-8') as f_obj:
        log_str = f_obj.read()
        lines = log_str.split('\n')
    return lines


def is_commit_head(line, is_merge):
    if not is_merge:
        return commit_id_line_pattern.match(line) is not None
    else:
        return merge_commit_id_line_pattern.match(line) is not None


def get_raw_numstats(project, is_merge):
    log_lines = load_numstat_lines(project, is_merge)
    line_number = len(log_lines)
    i = 0
    raw_git_numstats = list()
    while i < line_number:
        cur_l = log_lines[i]
        if is_commit_head(cur_l, is_merge):
            rgns = RawGitNumStat(is_merge)
            rgns.commit_id_line = cur_l
            i += 1
            if i < line_number:
                cur_l = log_lines[i]
            rgns.file_lines = list()
            while not is_commit_head(cur_l, is_merge):
                cur_l = cur_l.lstrip().rstrip()
                if cur_l != '':
                    rgns.file_lines.append(cur_l)
                i += 1
                if i >= line_number:
                    break
                cur_l = log_lines[i]
            raw_git_numstats.append(rgns)
        else:
            assert (cur_l.lstrip().rstrip() == '')
            return []
    return raw_git_numstats

# add for one commit
def get_one_raw_numstats(project, is_merge):
    log_lines = load_one_numstat_lines(project, is_merge)
    line_number = len(log_lines)
    i = 0
    raw_git_numstats = list()
    while i < line_number:
        cur_l = log_lines[i]
        if is_commit_head(cur_l, is_merge):
            rgns = RawGitNumStat(is_merge)
            rgns.commit_id_line = cur_l
            i += 1
            if i < line_number:
                cur_l = log_lines[i]
            rgns.file_lines = list()
            while not is_commit_head(cur_l, is_merge):
                cur_l = cur_l.lstrip().rstrip()
                if cur_l != '':
                    rgns.file_lines.append(cur_l)
                i += 1
                if i >= line_number:
                    break
                cur_l = log_lines[i]
            raw_git_numstats.append(rgns)
        else:
            assert (cur_l.lstrip().rstrip() == '')
            return []
    return raw_git_numstats


def get_numstats(project, is_merge=False, merge_all_log=False):
    raw_git_numstats = get_raw_numstats(project, is_merge)
    gns = dict()
    for rgn in raw_git_numstats:
        gn = GitNumStat(project)
        gn.from_raw_numstat(rgn)
        if not merge_all_log:
            gns[gn.commit_id] = gn
        else:
            if gn.base_commit is None:
                gns[gn.commit_id] = gn
            else:
                gns[gn.commit_id + '_' + gn.base_commit] = gn
    return gns

# add for one commit
def get_one_numstats(project, is_merge=False, merge_all_log=False):
    raw_git_numstats = get_one_raw_numstats(project, is_merge)
    gns = dict()
    for rgn in raw_git_numstats:
        gn = GitNumStat(project)
        gn.from_raw_numstat(rgn)
        if not merge_all_log:
            gns[gn.commit_id] = gn
        else:
            if gn.base_commit is None:
                gns[gn.commit_id] = gn
            else:
                gns[gn.commit_id + '_' + gn.base_commit] = gn
    return gns

if __name__ == '__main__':
    # get_numstats('hadoop-common')
    get_numstats('hadoop-common', True, True)
