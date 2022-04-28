import re

from defect_features.config import conf
from defect_features.git_analysis.git_stats.git_commit_meta import RawGitCommitMeta
from defect_features.git_analysis.git_stats.git_commit_meta import RawGitLog

commit_id_line_pattern = re.compile(r'commit: [0-9a-f]{40}')
parent_line_pattern = re.compile(r'parent')
author_line_pattern = re.compile(r'author:')
author_email_line_pattern = re.compile(r'author email:')
time_stamp_line_pattern = re.compile(r'time stamp:')
committer_line_pattern = re.compile(r'committer:')
committer_email_line_pattern = re.compile(r'committer email:')


def is_commit_head(lines, cur_idx):
    if cur_idx > len(lines) - 5:
        return False

    cur_line = lines[cur_idx]
    if commit_id_line_pattern.match(cur_line) is None:
        return False
    return True


def assign_line_value(rgl, lines, cur_line_num):
    assert(isinstance(rgl, RawGitLog))
    l = lines[cur_line_num]
    if parent_line_pattern.match(l):
        rgl.parent_line = l
        return True
    if author_line_pattern.match(l):
        rgl.author_line = l
        return True
    if author_email_line_pattern.match(l):
        rgl.email_line = l
        return True
    if time_stamp_line_pattern.match(l):
        rgl.time_stamp_line = l
    return False


def assign_head_to_rgl(lines, cur_idx):
    rgl = RawGitLog()
    assert(isinstance(rgl, RawGitLog))
    assert(commit_id_line_pattern.match(lines[cur_idx]) is not None)
    rgl.id_line = lines[cur_idx]
    cur_idx += 1
    assert(parent_line_pattern.match(lines[cur_idx]) is not None)
    rgl.parent_line = lines[cur_idx]
    cur_idx += 1
    assert(author_line_pattern.match(lines[cur_idx]) is not None)
    rgl.author_line = lines[cur_idx]
    cur_idx += 1
    assert(author_email_line_pattern.match(lines[cur_idx]) is not None)
    rgl.email_line = lines[cur_idx]
    cur_idx += 1
    assert(time_stamp_line_pattern.match(lines[cur_idx]) is not None)
    rgl.time_stamp_line = lines[cur_idx]
    cur_idx += 1
    assert(committer_line_pattern.match(lines[cur_idx]) is not None)
    rgl.committer_line = lines[cur_idx]
    cur_idx += 1
    assert(committer_email_line_pattern.match(lines[cur_idx]) is not None)
    rgl.committer_email_line = lines[cur_idx]
    return rgl, cur_idx + 1




def logstr_to_gitlogs(project, logstr):
    lines = logstr.split('\n')
    line_number = len(lines)
    i = 0
    git_logs = list()
    index_commit_id_map = dict()
    while i < line_number:
        assert(i < line_number - 4)
        rgl, i = assign_head_to_rgl(lines, i)
        rgl.commit_msg_lines = list()
        while not is_commit_head(lines, i):
            if i < line_number:
                rgl.commit_msg_lines.append(lines[i])
                i += 1
            else:
                break
        gl = RawGitCommitMeta(project)
        gl.from_raw_git_log(rgl)
        index_commit_id_map[gl.commit_id] = len(git_logs)
        git_logs.append(gl)

    for gl in git_logs:
        if len(gl.parent) == 0:
            continue
        for p in gl.parent:
            git_logs[index_commit_id_map[p]].add_son(gl.commit_id)

        i += 1
    return git_logs

def logstr_to_gitlogs_time(project, logstr):
    lines = logstr.split('\n')
    line_number = len(lines)
    i = 0
    git_logs = list()
    index_commit_id_map = dict()
    while i < line_number:
        assert(i < line_number - 4)
        rgl, i = assign_head_to_rgl(lines, i)
        rgl.commit_msg_lines = list()
        while not is_commit_head(lines, i):
            if i < line_number:
                rgl.commit_msg_lines.append(lines[i])
                i += 1
            else:
                break
        gl = RawGitCommitMeta(project)
        gl.from_raw_git_log(rgl)
        index_commit_id_map[gl.commit_id] = len(git_logs)
        git_logs.append(gl)
        i += 1
    return git_logs


# add for one commit
# def logstr_one_to_gitlogs(project, logstr):
#     lines = logstr.split('\n')
#     line_number = len(lines)
#     i = 0
#     git_logs = list()
#     index_commit_id_map = dict()
#     # while i < line_number:
#     assert(i < line_number - 4)
#     rgl, i = assign_head_to_rgl(lines, i)
#     rgl.commit_msg_lines = list()
#     while not is_commit_head(lines, i):
#         if i < line_number:
#             rgl.commit_msg_lines.append(lines[i])
#             i += 1
#         else:
#             break
#     gl = RawGitCommitMeta(project)
#     gl.from_one_raw_git_log(rgl)
#     index_commit_id_map[gl.commit_id] = len(git_logs)
#     git_logs.append(gl)
#
#     # for gl in git_logs:
#     #     if len(gl.parent) == 0:
#     #         continue
#     #     for p in gl.parent:
#     #         git_logs[index_commit_id_map[p]].add_son(gl.commit_id)
#     #     i += 1
#     return git_logs


def retrieve_git_logs(project_name):
    meta_log_path = conf.project_log_path(project_name, 'meta')
    # f_obj = file(meta_log_path, 'r')
    # log_str = f_obj.read()
    # f_obj.close()
    with open(meta_log_path,'r',encoding='utf-8') as f_obj:
        log_str = f_obj.read()
    git_logs = logstr_to_gitlogs(project_name, log_str)
    return git_logs

# add for one commit
def retrieve_git_log(project_name):
    meta_log_path = conf.project_log_path(project_name, 'meta2')
    with open(meta_log_path,'r',encoding='utf-8') as f_obj:
        log_str = f_obj.read()
    git_logs = logstr_to_gitlogs(project_name, log_str)
    return git_logs



def retrieve_git_logs_dict(project_name):
    git_logs = retrieve_git_logs(project_name)
    git_log_dict = dict()
    for gl in git_logs:
        git_log_dict[gl.commit_id] = gl
    return git_log_dict




def get_ancestors(git_logs, commit_id):
    git_log_dict = dict()
    for gl in git_logs:
        git_log_dict[gl.commit_id] = gl
    gl = git_log_dict[commit_id]
    ancestors = set()
    while len(gl.parent) == 1:
        ancestors |= set(gl.parent)
        gl = git_log_dict[gl.parent[0]]
    if len(gl.parent) >= 2:
        ancestors |= set(gl.parent)
        for p in gl.parent:
            ancestors |= get_ancestors(git_logs, p)
    return ancestors


if __name__ == '__main__':
    git_logs = retrieve_git_logs()
