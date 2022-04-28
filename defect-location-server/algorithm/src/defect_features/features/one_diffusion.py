#-*- coding: utf-8 -*-
from .git_one_commit_features import GitOneCommitFeatures
from defect_features.git_analysis.analyze_git_logs import retrieve_git_log
from defect_features.object.features import DiffusionFeatures as DiffusionFeaturesObj


class DiffusionFeatures(GitOneCommitFeatures):
    def __init__(self, rgcm):
        super(DiffusionFeatures, self).__init__(rgcm)

    def extract(self):
        # # ignore commit with the same timestamp
        # if self.check_identical_commit():
        #     return None
        ns = len(self.stats.modified_subsystems)
        nd = len(self.stats.modified_dirs)
        files, rename_files = self.stats.modified_files
        nf = 0
        for f, added, deleted in files:
            if self.in_required_extensions(f):
                nf += 1
        entropy = self.stats.entropy
        return {'project': self.project,
                'commit_id': self.commit_id,
                'ns': ns, 'nd': nd, 'nf': nf, 'entropy': entropy}



# add for one commit
# def extract_one_to_db_obj(project):
#     GitOneCommitFeatures.initialize(project)
#     rgcms = retrieve_git_log(project)
#     db_objs = list()
#     sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
#     for rgcm in sorted_rgcms:
#         df = DiffusionFeatures(rgcm)
#         attr_dict = df.extract()
#         if attr_dict is None:
#             continue
#         # df_obj type: <class 'defect_features.object.features.DiffusionFeatures'>
#         df_obj = DiffusionFeaturesObj(attr_dict)
#         # get diffusion attribute and construct df_obj ok
#         # df_obj.print_attributes()
#         db_objs.append(df_obj.to_db_obj())
#     return db_objs
def extract_one_to_db_obj(project):
    GitOneCommitFeatures.initialize(project)
    rgcms = retrieve_git_log(project)
    db_objs = list()
    sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
    rgcm = sorted_rgcms[-1]

    df = DiffusionFeatures(rgcm)
    attr_dict = df.extract()
        # if attr_dict is None:
        #     continue
    # df_obj type: <class 'defect_features.object.features.DiffusionFeatures'>
    df_obj = DiffusionFeaturesObj(attr_dict)
    # get diffusion attribute and construct df_obj ok
    # df_obj.print_attributes()
    df_dict = {'project': getattr(df_obj, 'project'), 'commit_id': getattr(df_obj, 'commit_id'),
               'ns': getattr(df_obj, 'ns'),
               'nd': getattr(df_obj, 'nd'), 'nf': getattr(df_obj, 'nf'), 'entropy': getattr(df_obj, 'entropy')}
    db_objs.append(df_dict)
    # db_objs.append(df_obj.to_db_obj())
    return db_objs


if __name__ == '__main__':
    project = 'ant'
    extract_one_to_db_obj(project)

