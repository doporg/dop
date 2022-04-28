from .analyze_git_numstat import is_commit_head
from defect_features.config import conf
from defect_features.git_analysis.git_stats.git_namestat import GitNameStat
from defect_features.git_analysis.git_stats.git_namestat import RawGitNameStat

def load_numstat_lines(project, is_merge):
    if not is_merge:
        log_path = conf.project_log_path(project, 'namestat')
    else:
        log_path = conf.project_log_path(project, 'merge_namestat')
    with open(log_path,'r',encoding='utf-8') as f_obj:
        log_str = f_obj.read()
        lines = log_str.split('\n')
    return lines

# add for one commit
def load_one_numstat_lines(project, is_merge):
    if not is_merge:
        log_path = conf.project_log_path(project, 'namestat2')
    else:
        log_path = conf.project_log_path(project, 'merge_namestat2')
    with open(log_path,'r',encoding='utf-8') as f_obj:
        log_str = f_obj.read()
        lines = log_str.split('\n')
    return lines


def get_raw_namestats(project, is_merge):
    log_lines = load_numstat_lines(project, is_merge)
    line_number = len(log_lines)
    i = 0
    raw_git_namestats = list()
    while i < line_number:
        cur_l = log_lines[i]
        if is_commit_head(cur_l, is_merge):
            rgns = RawGitNameStat(is_merge)
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
            raw_git_namestats.append(rgns)
        else:
            assert(cur_l.lstrip().rstrip() == '')
            return []
    return raw_git_namestats

# add for one commit
def get_one_raw_namestats(project, is_merge):
    log_lines = load_one_numstat_lines(project, is_merge)
    line_number = len(log_lines)
    i = 0
    raw_git_namestats = list()
    while i < line_number:
        cur_l = log_lines[i]
        if is_commit_head(cur_l, is_merge):
            rgns = RawGitNameStat(is_merge)
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
            raw_git_namestats.append(rgns)
        else:
            assert(cur_l.lstrip().rstrip() == '')
            return []
    return raw_git_namestats


def retrieve_git_namestats(project, is_merge=False, merge_all_log=False):
    rgns = get_raw_namestats(project, is_merge)
    gns_dict = dict()
    for rgn in rgns:
        gn = GitNameStat(project)
        gn.from_raw_git_namestat(rgn)
        if not merge_all_log:
            gns_dict[gn.commit_id] = gn
        else:
            if gn.base_commit is None:
                gns_dict[gn.commit_id] = gn
            else:
                gns_dict[gn.commit_id+'_'+gn.base_commit] = gn
    return gns_dict

# add for one commit
def retrieve_one_git_namestats(project, is_merge=False, merge_all_log=False):
    rgns = get_one_raw_namestats(project, is_merge)
    gns_dict = dict()
    for rgn in rgns:
        gn = GitNameStat(project)
        gn.from_raw_git_namestat(rgn)
        if not merge_all_log:
            gns_dict[gn.commit_id] = gn
        else:
            if gn.base_commit is None:
                gns_dict[gn.commit_id] = gn
            else:
                gns_dict[gn.commit_id+'_'+gn.base_commit] = gn
    return gns_dict


if __name__ == '__main__':
    retrieve_git_namestats('hadoop-common', True, True)