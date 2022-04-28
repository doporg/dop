#-*- coding: utf-8 -*-
import sys


import json
import datetime
from defect_features.object import commit_meta
from defect_features.git_analysis.analyze_git_logs import retrieve_git_logs
from defect_features.git_analysis.analyze_git_logs import retrieve_git_log
from defect_features.features import diffusion
from defect_features.features import experience
from defect_features.features import history
from defect_features.features import purpose
from defect_features.features import size
from defect_features.db.api import *


class LogFeatures:

    def store_meta(self, project, startTime):
        gls = retrieve_git_logs(project)
        db_objs = list()
        # print('number of commits:',len(gls))
        # print(project, 'Begin to store meta data')
        startTime = datetime.datetime.strptime(startTime, "%Y-%m-%d")

        for gl in gls:
            cm = commit_meta.CommitMeta()
            cm.from_git_log(gl)

            if getattr(cm, 'creation_time') < startTime:
                continue
            cm_dict = {'project':getattr(cm, 'project'),'commit_id':getattr(cm, 'commit_id'),
                      'is_merge':getattr(cm, 'is_merge'), 'time_stamp':getattr(cm, 'time_stamp'),
                      'author_email':getattr(cm, 'author_email')}
            db_objs.append(cm_dict)

        cmpath = conf.local_path+"featuresStore/commit_meta"
        with open(cmpath, 'w') as f_meta:
            json.dump(db_objs, f_meta)




    def store_features(self, project, feature_type):
        # get db object
        if feature_type == 'diffusion':
            # print (project,'Begin to store diffusion features')
            df_objs = diffusion.extract_to_db_obj(project)
            with open(conf.local_path+"featuresStore/diffusion_features",
                      'w') as f_diffusion:
                json.dump(df_objs, f_diffusion)

        elif feature_type == 'experience':
            # print(project,'Begin to store experience features')
            ef_objs = experience.extract_to_db_obj(project)
            with open(conf.local_path+"featuresStore/experience_features",
                      'w') as f_experience:
                json.dump(ef_objs, f_experience)

        elif feature_type == 'history':
            # print(project,'Begin to store history features')
            hf_objs = history.extract_to_db_obj(project)
            with open(conf.local_path+"featuresStore/history_features",
                      'w') as f_history:
                json.dump(hf_objs, f_history)

        elif feature_type == 'purpose':
            # print(project, 'Begin to store purpose features')
            pf_objs = purpose.extract_to_db_obj(project)
            with open(conf.local_path+"featuresStore/purpose_features",
                      'w') as f_purpose:
                json.dump(pf_objs, f_purpose)
        else:
            assert(feature_type == 'size')
            # print(project,'Begin to store size features')
            sf_objs = size.extract_to_db_obj(project)
            with open(conf.local_path+"featuresStore/size_features",
                      'w') as f_size:
                json.dump(sf_objs, f_size)



    def log_feature(self, startTime):
        for p in conf.projects:
            # start = time.time()
            # print(p)
            self.store_meta(p, startTime)
            self.store_features(p, 'purpose')
            self.store_features(p, 'diffusion')
            self.store_features(p, 'experience')
            self.store_features(p, 'history')
            self.store_features(p, 'size')


            # end = time.time()
            # print('Project %s using time(min): %s' % (p, (end-start)/60))



# if __name__ == '__main__':
#     for p in conf.projects:
#         start = time.time()
#         print(p)
#         store_one_meta(p)
#         store_one_features(p, 'diffusion')
#         store_one_features(p, 'experience')
#         store_one_features(p, 'purpose')
#         store_one_features(p, 'history')
#         store_one_features(p, 'size')

        # store_meta(p)
        # store_features(p, 'diffusion')
        # store_features(p, 'experience')
        # store_features(p, 'purpose')
        # store_features(p, 'history')
        # store_features(p, 'size')
        # end = time.time()
        # print('Project %s using time(min): %s' % (p, (end-start)/60))
