#-*- coding: utf-8 -*-
from .git_one_commit_features import GitOneCommitFeatures
from copy import deepcopy
from defect_features.utils.extensions import in_our_extensions
from defect_features.git_analysis.analyze_git_logs import retrieve_git_logs
from defect_features.git_analysis.analyze_git_logs import retrieve_git_log
from defect_features.object.features import SizeFeatures as SizeFeaturesObj



class SizeFeatures(GitOneCommitFeatures):
    mem = 0
    def __init__(self, rgcm):
        super(SizeFeatures, self).__init__(rgcm)

    def evolve_from_prior_commit(self):
        la = 0
        ld = 0
        lt = 0
        nf = 0
        gcf = GitOneCommitFeatures
        stats = self.stats
        namestats = self.namestat
        if len(self.parents) == 0:
            p = None
        elif len(self.parents) == 1:
            p = self.parents[0]
        else:
            if gcf.project_merge_numstat[self.commit_id].base_commit is not None:
                p = gcf.project_merge_numstat[self.commit_id].base_commit
                stats = gcf.project_merge_numstat[self.commit_id]
                namestats = gcf.project_merge_namestat[self.commit_id]
            else:
                p = self.parents[0]
                stats = None
        if stats is not None:
            files, rename_files = stats.modified_files
        else:
            # merge后和两个分支对比都没有变化
            files = []
            rename_files = []
        if p is not None:
            file_stats = gcf.parent_file_stats[p]['files']
            if gcf.parent_file_stats[p]['son_num'] == 1:
                gcf.parent_file_stats[self.commit_id]['files'] = file_stats
            else:
                # 新建分支，file_stats deepcopy一份
                gcf.parent_file_stats[self.commit_id]['files'] = deepcopy(file_stats)

        for f, added, deleted in files:
            if namestats.file_modify_type[f] == 'add':
                assert (deleted == 0)
                #
                gcf.parent_file_stats[self.commit_id]['files'][f] = added
                if in_our_extensions(f):
                    nf += 1
                    la += added
            elif namestats.file_modify_type[f] == 'delete':
                assert (added == 0)
                #assert(deleted == file_stats[f])

                if in_our_extensions(f):
                    lt += file_stats[f]
                    nf += 1
                    ld += deleted
                #
                del gcf.parent_file_stats[self.commit_id]['files'][f]
            elif namestats.file_modify_type[f] == 'rename':
                cur_file = rename_files[f]
                tmp = file_stats[f]
                assert (tmp + added - deleted >= 0)
                #
                gcf.parent_file_stats[self.commit_id]['files'][cur_file] = tmp + added - deleted
                if in_our_extensions(f) or in_our_extensions(cur_file):
                    lt += tmp
                    nf += 1
                    la += added
                    ld += deleted
                #
                del gcf.parent_file_stats[self.commit_id]['files'][f]
            else:
                assert (namestats.file_modify_type[f] == 'modify')
                tmp = file_stats[f]
                assert (tmp + added - deleted >= 0)
                #
                gcf.parent_file_stats[self.commit_id]['files'][f] = tmp + added - deleted
                if in_our_extensions(f):
                    lt += tmp
                    nf += 1
                    la += added
                    ld += deleted
        if len(self.parents) > 1:
            lt = 0
            la = 0
            ld = 0
        else:
            nf = len(files)
            if nf > 0:
                lt = 1.0 * lt / nf
        return lt, la, ld

    def extract(self):
        # # ignore commit with the same timestamp
        # if self.check_identical_commit():
        #     return None
        gcf = GitOneCommitFeatures
        gcf.parent_file_stats[self.commit_id] = dict()
        gcf.parent_file_stats[self.commit_id]['files'] = dict()
        gcf.parent_file_stats[self.commit_id]['son_num'] = len(self.sons)
        lt, la, ld = self.evolve_from_prior_commit()
        lt = round(lt, 2)
        return {'project': self.project,
                'commit_id': self.commit_id,
                'la': la, 'ld': ld, 'lt': lt}


# add for one commit
def extract_one_to_db_obj(project):
    gcf = GitOneCommitFeatures
    gcf.initialize(project)
    rgcms = retrieve_git_log(project)
    db_objs = list()
    root = set()
    rgcm_dict = dict()
    for rgcm in rgcms:
        rgcm_dict[rgcm.commit_id] = rgcm
        if len(rgcm.parent) == 0:
            root.add(rgcm.commit_id)
    del rgcms
    gcf.current_root = root
    gcf.calculated_commit = set()
    gcf.candidate_commit = set()
    gcf.rgcm_dict = rgcm_dict
    while len(SizeFeatures.current_root) > 0:
        extract_results = gcf.calculate_features_for_root(SizeFeatures)
        assert(isinstance(extract_results, list))
        for er in extract_results:
            sf_obj = SizeFeaturesObj(er)
            #sf_obj.print_attributes()
            sf_dict = {'project': getattr(sf_obj, 'project'), 'commit_id': getattr(sf_obj, 'commit_id'),
                       'la': getattr(sf_obj, 'la'),
                       'ld': getattr(sf_obj, 'ld'), 'lt': getattr(sf_obj, 'lt')}
            db_objs.append(sf_dict)
            # db_objs.append(sf_obj.to_db_obj())
    return db_objs

# def extract_one_to_db_obj(project):
#     gcf = GitOneCommitFeatures
#     gcf.initialize(project)
#     rgcms = retrieve_git_log(project)
#     db_objs = list()
#     root = set()
#     rgcm_dict = dict()
#     sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
#     rgcm = sorted_rgcms[-1]
#     rgcm_dict[rgcm.commit_id] = rgcm
#     if len(rgcm.parent) == 0:
#         root.add(rgcm.commit_id)
#     del rgcms
#     gcf.current_root = root
#     gcf.calculated_commit = set()
#     gcf.candidate_commit = set()
#     gcf.rgcm_dict = rgcm_dict
#     while len(SizeFeatures.current_root) > 0:
#         extract_results = gcf.calculate_features_for_root(SizeFeatures)
#         assert(isinstance(extract_results, list))
#         for er in extract_results:
#             sf_obj = SizeFeaturesObj(er)
#             #sf_obj.print_attributes()
#             db_objs.append(sf_obj.to_db_obj())
#     return db_objs

if __name__ == '__main__':
    project = 'ant'
    extract_one_to_db_obj(project)


