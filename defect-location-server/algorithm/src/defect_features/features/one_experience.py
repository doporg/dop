#-*- coding: utf-8 -*-
from .git_one_commit_features import GitOneCommitFeatures
from defect_features.git_analysis.analyze_git_logs import retrieve_git_log
from defect_features.object.features import ExperienceFeatures as ExperienceFeaturesObj


class ExperienceFeatures(GitOneCommitFeatures):
    def __init__(self, rgcm):
        super(ExperienceFeatures, self).__init__(rgcm)

    def extract(self):
        # # ignore commit with the same timestamp
        # if self.check_identical_commit():
        #     return None
        subs = self.stats.modified_subsystems
        gcf = GitOneCommitFeatures
        try:
            gcf.developer_stats[self.author_email]
        except KeyError:
            gcf.developer_stats[self.author_email] = dict()
            gcf.developer_stats[self.author_email]['changes'] = list()
            gcf.developer_stats[self.author_email]['subs'] = dict()

        exp = len(gcf.developer_stats[self.author_email]['changes'])
        rexp = 0
        sexp = 0
        if exp > 0:
            for time_stamp in gcf.developer_stats[self.author_email]['changes']:
                denominator = 1.0 * (self.time_stamp - time_stamp) / 86400 / 365 + 1
                rexp += 1.0 / denominator

        for subsys in subs:
            try:
                gcf.developer_stats[self.author_email]['subs'][subsys]
            except KeyError:
                gcf.developer_stats[self.author_email]['subs'][subsys] = 0
            sexp += gcf.developer_stats[self.author_email]['subs'][subsys]
            gcf.developer_stats[self.author_email]['subs'][subsys] += 1
        gcf.developer_stats[self.author_email]['changes'].append(self.time_stamp)
        rexp = round(rexp, 2)
        if len(subs) != 0:
            sexp /= len(subs)
        return {
            'project': self.project,
            'commit_id': self.commit_id,
            'exp': exp,
            'rexp': rexp,
            'sexp': sexp
        }



# add for one commit
# def extract_one_to_db_obj(project):
#     GitOneCommitFeatures.initialize(project)
#     rgcms = retrieve_git_log(project)
#     sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
#     db_objs = list()
#     for rgcm in sorted_rgcms:
#         ef = ExperienceFeatures(rgcm)
#         attr_dict = ef.extract()
#         if attr_dict is None:
#             continue
#         ef_obj = ExperienceFeaturesObj(attr_dict)
#         #ef_obj.print_attributes()
#         db_objs.append(ef_obj.to_db_obj())
#     return db_objs
def extract_one_to_db_obj(project):
    GitOneCommitFeatures.initialize(project)
    rgcms = retrieve_git_log(project)
    sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
    db_objs = list()
    rgcm = sorted_rgcms[-1]
    ef = ExperienceFeatures(rgcm)
    attr_dict = ef.extract()
        # if attr_dict is None:
        #     continue
    ef_obj = ExperienceFeaturesObj(attr_dict)
    #ef_obj.print_attributes()
    ef_dict = {'project': getattr(ef_obj, 'project'), 'commit_id': getattr(ef_obj, 'commit_id'),
               'exp': getattr(ef_obj, 'exp'),
               'rexp': getattr(ef_obj, 'rexp'), 'sexp': getattr(ef_obj, 'sexp')}
    db_objs.append(ef_dict)

    # db_objs.append(ef_obj.to_db_obj())
    return db_objs

if __name__ == '__main__':
    project = 'ant'
    extract_one_to_db_obj(project)



