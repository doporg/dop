#-*- coding: utf-8 -*-
from .git_one_commit_features import GitOneCommitFeatures
from copy import deepcopy
from defect_features.git_analysis.analyze_git_logs import retrieve_git_logs
from defect_features.git_analysis.analyze_git_logs import retrieve_git_log
from defect_features.object.features import HistoryFeatures as HistoryFeaturesObj


class HistoryFeatures(GitOneCommitFeatures):
    mem = 0
    Non_merge= 'history_sep_non_merge'
    Merge='history_sep_merge'
    def __init__(self, rgcm):
        super(HistoryFeatures, self).__init__(rgcm)

    def create_file_record(self, path):
        gcf = GitOneCommitFeatures
        tmp = dict()
        tmp['developers'] = set()
        tmp['developers'].add(self.author_email)
        tmp['changes'] = set()
        tmp['changes'].add((self.committer_email, self.time_stamp))
        tmp['last_age'] = self.time_stamp
        gcf.mem_manager.get(self.commit_id)[path] = tmp

    def evolve_non_merge(self):
        assert(len(self.parents) <= 1)
        gcf = GitOneCommitFeatures
        stats = self.stats
        namestats = self.namestat
        dev_set = set()
        age = 0
        change_set = set()
        files, rename_files = stats.modified_files
        del stats
        p = None
        p_file_stats = None
        nf = 0
        if len(self.parents) == 1:
            # 获取Parent commit的file_stats
            p = self.parents[0]
            p_file_stats = gcf.mem_manager.get(p)
            # 更新parent_commit 计数器
            gcf.mem_manager.update_counter(p)
            # 如果parent commit只有一个son commit，file_stats 浅复制， 否则 深复制
            if gcf.parent_file_stats[p]['son_num'] == 1:
                gcf.mem_manager.copy_stats(p,self.commit_id)
            else:
                gcf.mem_manager.deepcopy_stats(p,self.commit_id)
        else:
            gcf.mem_manager.debut(self.commit_id)
        for f, added, deleted in files:
            if namestats.file_modify_type[f] == 'add':
                assert (deleted == 0)
                if self.in_required_extensions(f):
                    nf += 1
                self.create_file_record(f)
            elif namestats.file_modify_type[f] == 'delete':
                assert (p is not None)
                try:
                    if self.in_required_extensions(f):
                        nf += 1
                        dev_set |= p_file_stats[f]['developers']
                        change_set |= p_file_stats[f]['changes']
                        age += 1.0 * (self.time_stamp - p_file_stats[f]['last_age']) / 86400.0
                    del gcf.mem_manager.get(self.commit_id)[f]
                except Exception as e:
                    print('history delete error', e,self.commit_id)
                    continue
            elif namestats.file_modify_type[f] == 'rename':
                # 重命名文件
                # 深复制 原文件的file_stats内容，并更新
                # 删除 原文件 file_stats
                assert (p is not None)
                try:
                    cur_file = rename_files[f]
                    if self.in_required_extensions(f):
                        nf += 1
                        dev_set |= p_file_stats[f]['developers']
                        change_set |= p_file_stats[f]['changes']
                        age += 1.0 * (self.time_stamp - p_file_stats[f]['last_age']) / 86400.0
                    gcf.mem_manager.get(self.commit_id)[cur_file] = deepcopy(p_file_stats[f])
                    gcf.mem_manager.get(self.commit_id)[cur_file]['developers'].add(self.author_email)
                    gcf.mem_manager.get(self.commit_id)[cur_file]['changes'].add((self.committer_email,
                                                                                             self.time_stamp))
                    gcf.mem_manager.get(self.commit_id)[cur_file]['last_age'] = self.time_stamp
                    del gcf.mem_manager.get(self.commit_id)[f]
                except Exception as e:
                    print('history rename error', e,self.commit_id)
                    continue
            else:
                assert (namestats.file_modify_type[f] == 'modify')
                assert (p is not None)
                try:
                    if self.in_required_extensions(f):
                        nf += 1
                        # ndev nuc 都是 unique
                        dev_set |= p_file_stats[f]['developers']
                        change_set |= p_file_stats[f]['changes']
                        age += 1.0 * (self.time_stamp - p_file_stats[f]['last_age']) / 86400.0
                    gcf.mem_manager.get(self.commit_id)[f]['developers'].add(self.author_email)
                    gcf.mem_manager.get(self.commit_id)[f]['changes'].add((self.committer_email,
                                                                                      self.time_stamp))
                    gcf.mem_manager.get(self.commit_id)[f]['last_age'] = self.time_stamp
                except Exception as e:
                    print('history modify error', e, self.commit_id)
                    continue
        # 未取平均
        ndev = len(dev_set)
        nuc = len(change_set)
        if nf > 0:
            # ndev = 1.0 * ndev / nf
            nuc = 1.0 * nuc / nf
            age = age / nf
        # ndev = round(ndev, 2)
        age = round(age, 2)
        nuc = round(nuc, 2)
        return {
            'project': self.project,
            'commit_id': self.commit_id,
            'ndev': ndev,
            'age': age,
            'nuc': nuc
        }

    def evolve_merge(self):
        # 直接返回0
        gcf = GitOneCommitFeatures
        assert(len(self.parents) > 1)
        # 先承接并整合parent commit的文件信息
        gcf.mem_manager.debut(self.commit_id)
        for p in self.parents:
            p_file_stats = gcf.mem_manager.get(p)
            # 更新parent_commit 计数器
            gcf.mem_manager.update_counter(p)
            file_paths = p_file_stats.keys()
            for f in file_paths:
                if f not in gcf.mem_manager.get(self.commit_id):
                    gcf.mem_manager.get(self.commit_id)[f] = dict()
                    gcf.mem_manager.get(self.commit_id)[f]['developers'] = set()
                    gcf.mem_manager.get(self.commit_id)[f]['changes'] = set()
                    gcf.mem_manager.get(self.commit_id)[f]['last_age'] = \
                        p_file_stats[f]['last_age']
                    gcf.mem_manager.get(self.commit_id)[f]['developers'] |= \
                        p_file_stats[f]['developers']
                gcf.mem_manager.get(self.commit_id)[f]['changes'] |= p_file_stats[f]['changes']
                if gcf.mem_manager.get(self.commit_id)[f]['last_age'] < \
                        p_file_stats[f]['last_age']:
                    gcf.mem_manager.get(self.commit_id)[f]['last_age'] = \
                        p_file_stats[f]['last_age']
        added_files = set()
        renamed_cur_files = set()
        for p in self.parents:
            try:
                namestats = gcf.project_merge_namestat[self.commit_id+'_'+p]
                stats = gcf.project_merge_numstat[self.commit_id+'_'+p]
            except KeyError:
                continue
            files, rename_files = stats.modified_files
            for f, added, deleted in files:
                try:
                    p_file_stats = gcf.mem_manager.get(self.commit_id)[f]
                except KeyError:
                    if namestats.file_modify_type[f] in ['delete', 'rename']:
                        continue
                    elif namestats.file_modify_type[f] == 'add':
                        added_files.add(f)
                if namestats.file_modify_type[f] == 'delete':
                    try:
                        assert (added == 0 and p is not None)
                        del gcf.mem_manager.get(self.commit_id)[f]
                    except Exception as e:
                        print('history merge delete error', e)
                        continue
                elif namestats.file_modify_type[f] == 'rename':
                    assert (p is not None)
                    cur_file = rename_files[f]
                    renamed_cur_files.add(cur_file)
                    try:
                        gcf.mem_manager.get(self.commit_id)[cur_file]
                    except KeyError:
                        gcf.mem_manager.get(self.commit_id)[cur_file] = dict()
                        gcf.mem_manager.get(self.commit_id)[cur_file]['developers'] = set()
                        gcf.mem_manager.get(self.commit_id)[cur_file]['changes'] = set()
                        gcf.mem_manager.get(self.commit_id)[cur_file]['last_age'] = \
                            p_file_stats['last_age']
                    try:
                        gcf.mem_manager.get(self.commit_id)[cur_file]['developers'] |= \
                            p_file_stats['developers']
                        gcf.mem_manager.get(self.commit_id)[cur_file]['changes'] |= \
                            p_file_stats['changes']
                        if gcf.mem_manager.get(self.commit_id)[cur_file]['last_age'] < \
                                p_file_stats['last_age']:
                            gcf.mem_manager.get(self.commit_id)[cur_file]['last_age'] = \
                                p_file_stats['last_age']
                        del gcf.mem_manager.get(self.commit_id)[f]
                    except Exception as e:
                        print('history merge delete error', e)
                        continue

        for added_f in added_files:
            if added_f not in renamed_cur_files:
                self.create_file_record(added_f)
        for p in self.parents:
            if gcf.parent_file_stats[p]['son_num'] <= 1:
                continue
            else:
                gcf.parent_file_stats[p]['son_num'] -= 1
        return {'project': self.project,
                'commit_id': self.commit_id,
                'ndev': 0, 'age': 0, 'nuc': 0}
    def extract(self):
        # # ignore commit with the same timestamp
        # if self.check_identical_commit():
        #     return None
        gcf = GitOneCommitFeatures
        gcf.parent_file_stats[self.commit_id] = dict()
        gcf.parent_file_stats[self.commit_id]['son_num'] = len(self.sons)
        if len(self.parents) <= 1:
            return self.evolve_non_merge()
        else:
            return self.evolve_merge()

# add for one commit
def extract_one_to_db_obj(project):
    gcf = GitOneCommitFeatures
    gcf.initialize(project,merge_all_log=True)
    rgcms = retrieve_git_log(project)
    db_objs = list()
    root = set()
    rgcm_dict = dict()
    for rgcm in rgcms:
        rgcm_dict[rgcm.commit_id] = rgcm
        if len(rgcm.parent) == 0:
            root.add(rgcm.commit_id)
    gcf.current_root = root
    gcf.calculated_commit = set()
    gcf.candidate_commit = set()
    gcf.rgcm_dict = rgcm_dict
    number = 0
    while len(gcf.current_root) > 0:
        number += len(gcf.current_root)
        extract_results = gcf.calculate_features_for_root(HistoryFeatures)
        assert(isinstance(extract_results, list))
        for er in extract_results:
            hf_obj = HistoryFeaturesObj(er)
            hf_dict = {'project': getattr(hf_obj, 'project'), 'commit_id': getattr(hf_obj, 'commit_id'),
                       'ndev': getattr(hf_obj, 'ndev'),
                       'age': getattr(hf_obj, 'age'), 'nuc': getattr(hf_obj, 'nuc')}
            db_objs.append(hf_dict)
            # db_objs.append(sf_obj.to_db_obj())
    return db_objs
# def extract_one_to_db_obj(project):
#     gcf = GitOneCommitFeatures
#     gcf.initialize(project,merge_all_log=True)
#     rgcms = retrieve_git_log(project)
#     db_objs = list()
#     root = set()
#     rgcm_dict = dict()
#     rgcm = rgcms[-1]
#
#     rgcm_dict[rgcm.commit_id] = rgcm
#     if len(rgcm.parent) == 0:
#         root.add(rgcm.commit_id)
#     gcf.current_root = root
#     gcf.calculated_commit = set()
#     gcf.candidate_commit = set()
#     gcf.rgcm_dict = rgcm_dict
#     number = 0
#     while len(gcf.current_root) > 0:
#         number += len(gcf.current_root)
#         extract_results = gcf.calculate_features_for_root(HistoryFeatures)
#         assert(isinstance(extract_results, list))
#         for er in extract_results:
#             sf_obj = HistoryFeaturesObj(er)
#             db_objs.append(sf_obj.to_db_obj())
#     return db_objs


if __name__ == '__main__':
    project = 'scmcenter'
    extract_one_to_db_obj(project)

