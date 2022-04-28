import sys
from defect_features.config import conf
import pandas as pd
import os
import json

class FeatureCombination:


    def loadjson_a(self, project):
        with open(conf.local_path+"featuresStore/commit_meta", 'r') as load_f:
            commits_meta = json.load(load_f)
        with open(conf.local_path+"featuresStore/purpose_features", 'r') as load_f:
            purpose_features = json.load(load_f)

        commits_dict = dict()

        for elem in commits_meta:
            commits_dict[elem['commit_id']] = {'commit_id': elem['commit_id'], 'project': elem['project'],
                                            'time_stamp': elem['time_stamp'],
                                            'author_email': elem['author_email'],
                                            'is_merge': elem['is_merge']}

        for elem in purpose_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['is_fix'] = elem['is_fix']
                commits_dict[elem['commit_id']]['classification'] = elem['classification']
                commits_dict[elem['commit_id']]['contains_bug'] = elem['contains_bug']
                commits_dict[elem['commit_id']]['fix_by'] = elem['fix_by']
                commits_dict[elem['commit_id']]['fixes'] = elem['fixes']
                commits_dict[elem['commit_id']]['find_interval'] = elem['find_interval']
                commits_dict[elem['commit_id']]['fix_file_num'] = elem['fix_file_num']
                commits_dict[elem['commit_id']]['buggy_lines'] = elem['buggy_lines']
                commits_dict[elem['commit_id']]['clean_lines'] = elem['clean_lines']

        df = pd.DataFrame(commits_dict).T
        save_path = os.path.join(conf.local_path + 'defect_features/utils/data_tmp', project +'__'+conf.modelName + '_a.csv')
        df.to_csv(save_path, index=False,
                  columns=['commit_id','project','time_stamp','author_email',
                                             'is_merge','is_fix','classification','contains_bug',
                                             'fix_by','find_interval','buggy_lines','fixes','fix_file_num','clean_lines'])

    def loadjson_v(self, project):
        with open(conf.local_path+"featuresStore/commit_meta", 'r') as load_f:
            commits_meta = json.load(load_f)
        with open(conf.local_path+"featuresStore/history_features", 'r') as load_f:
            history_features = json.load(load_f)
        with open(conf.local_path+"featuresStore/experience_features", 'r') as load_f:
            experience_features = json.load(load_f)
        with open(conf.local_path+"featuresStore/size_features", 'r') as load_f:
            size_features = json.load(load_f)
        with open(conf.local_path+"featuresStore/diffusion_features", 'r') as load_f:
            diffusion_features = json.load(load_f)

        commits_dict = dict()
        for elem in commits_meta:
            commits_dict[elem['commit_id']] = {'commit_id': elem['commit_id'], 'project': elem['project']}


        for elem in history_features:
            if elem['commit_id'] not in commits_dict:
                continue
            # commits_dict[elem['commit_id']] = {'commit_id':elem['commit_id'],'project':elem['project'],
            #                          'ndev':elem['ndev'],'age':elem['age'],'nuc':elem['nuc']}
            else:
                commits_dict[elem['commit_id']]['ndev'] = elem['ndev']
                commits_dict[elem['commit_id']]['age'] = elem['age']
                commits_dict[elem['commit_id']]['nuc'] = elem['nuc']
        for elem in experience_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['exp'] = elem['exp']
                commits_dict[elem['commit_id']]['rexp'] = elem['rexp']
                commits_dict[elem['commit_id']]['sexp'] = elem['sexp']
        for elem in size_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['la'] = elem['la']
                commits_dict[elem['commit_id']]['ld'] = elem['ld']
                commits_dict[elem['commit_id']]['lt'] = elem['lt']
        for elem in diffusion_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['ns'] = elem['ns']
                commits_dict[elem['commit_id']]['nd'] = elem['nd']
                commits_dict[elem['commit_id']]['nf'] = elem['nf']
                commits_dict[elem['commit_id']]['entropy'] = elem['entropy']

        df = pd.DataFrame(commits_dict).T
        save_path = os.path.join(conf.local_path+'defect_features/utils/data_tmp',project+'__'+conf.modelName + '_v.csv')
        df.to_csv(save_path,index=False,columns=['commit_id','project','ndev','age','nuc',
                                             'exp','rexp','sexp','la','ld','lt','ns',
                                             'nd','nf','entropy'])

    def loadjson_a_one(self, project):
        with open(conf.local_path+"featuresStore/commit_meta1", 'r') as load_f:
            commits_meta = json.load(load_f)
        with open(conf.local_path+"featuresStore/purpose_features1", 'r') as load_f:
            purpose_features = json.load(load_f)

        commits_dict = dict()

        for elem in commits_meta:
            commits_dict[elem['commit_id']] = {'commit_id': elem['commit_id'], 'project': elem['project'],
                                               'time_stamp': elem['time_stamp'],
                                               'author_email': elem['author_email'],
                                               'is_merge': elem['is_merge']}

        for elem in purpose_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['is_fix'] = elem['is_fix']
                commits_dict[elem['commit_id']]['classification'] = elem['classification']
                commits_dict[elem['commit_id']]['contains_bug'] = elem['contains_bug']
                commits_dict[elem['commit_id']]['fix_by'] = elem['fix_by']
                commits_dict[elem['commit_id']]['fixes'] = elem['fixes']
                commits_dict[elem['commit_id']]['find_interval'] = elem['find_interval']
                commits_dict[elem['commit_id']]['fix_file_num'] = elem['fix_file_num']
                commits_dict[elem['commit_id']]['buggy_lines'] = elem['buggy_lines']
                commits_dict[elem['commit_id']]['clean_lines'] = elem['clean_lines']

        df = pd.DataFrame(commits_dict).T
        save_path = os.path.join(conf.local_path + 'defect_features/utils/data_tmp', project + '_one_a.csv')
        df.to_csv(save_path, index=False,
                  columns=['commit_id', 'project', 'time_stamp', 'author_email',
                           'is_merge', 'is_fix', 'classification', 'contains_bug',
                           'fix_by', 'find_interval', 'buggy_lines', 'fixes', 'fix_file_num', 'clean_lines'])

    def loadjson_v_one(self, project):
        with open(conf.local_path+"featuresStore/history_features1", 'r') as load_f:
            history_features = json.load(load_f)
        with open(conf.local_path+"featuresStore/experience_features1", 'r') as load_f:
            experience_features = json.load(load_f)
        with open(conf.local_path+"featuresStore/size_features1", 'r') as load_f:
            size_features = json.load(load_f)
        with open(conf.local_path+"featuresStore/diffusion_features1", 'r') as load_f:
            diffusion_features = json.load(load_f)

        commits_dict = dict()
        for elem in experience_features:
            commits_dict[elem['commit_id']] = {'commit_id':elem['commit_id'], 'project':elem['project'], 'exp':elem['exp'],
             'rexp':elem['rexp'], 'sexp':elem['sexp']}

        for elem in history_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['ndev'] = elem['ndev']
                commits_dict[elem['commit_id']]['age'] = elem['age']
                commits_dict[elem['commit_id']]['nuc'] = elem['nuc']
        for elem in size_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['la'] = elem['la']
                commits_dict[elem['commit_id']]['ld'] = elem['ld']
                commits_dict[elem['commit_id']]['lt'] = elem['lt']
        for elem in diffusion_features:
            if elem['commit_id'] not in commits_dict:
                continue
            else:
                commits_dict[elem['commit_id']]['ns'] = elem['ns']
                commits_dict[elem['commit_id']]['nd'] = elem['nd']
                commits_dict[elem['commit_id']]['nf'] = elem['nf']
                commits_dict[elem['commit_id']]['entropy'] = elem['entropy']

        df = pd.DataFrame(commits_dict).T
        save_path = os.path.join(conf.local_path+'defect_features/utils/data_tmp',project + '_one_v.csv')
        df.to_csv(save_path,index=False,columns=['commit_id','project','ndev','age','nuc',
                                             'exp','rexp','sexp','la','ld','lt','ns',
                                             'nd','nf','entropy'])



    def combination(self, project):
        read_path_v = os.path.join(conf.local_path+'defect_features/utils/data_tmp',project+'__'+conf.modelName + '_v.csv')
        df_v = pd.read_csv(read_path_v).set_index('commit_id').T
        dict_v = df_v.to_dict()
        read_path_a = os.path.join(conf.local_path+'defect_features/utils/data_tmp', project+'__'+conf.modelName + '_a.csv')
        df_a = pd.read_csv(read_path_a).set_index('commit_id').T
        dict_a = df_a.to_dict()
        for commit_id,elem in dict_a.items():
            elem.update(dict_v[commit_id])
            elem.update({'commit_id':commit_id})
        df = pd.DataFrame(dict_a).T.sort_values(by=['time_stamp'],ascending=False)
        save_path = os.path.join(conf.local_path+'defect_features/report',project+'__'+conf.modelName+'.csv')
        columns_write = ['is_fix', 'ns', 'nd', 'nf','entropy', 'la', 'ld', 'lt', 'ndev', 'age', 'nuc', 'exp', 'rexp', 'sexp', 'contains_bug']
        df.to_csv(save_path,index=False,columns=columns_write)

    # add for one commit
    def combination_one(self, project):
        read_path_v = os.path.join(conf.local_path+'defect_features/utils/data_tmp',project + '_one_v.csv')
        df_v = pd.read_csv(read_path_v).set_index('commit_id').T
        dict_v = df_v.to_dict()
        read_path_a = os.path.join(conf.local_path+'defect_features/utils/data_tmp', project + '_one_a.csv')
        df_a = pd.read_csv(read_path_a).set_index('commit_id').T
        dict_a = df_a.to_dict()
        for commit_id,elem in dict_a.items():
            elem.update(dict_v[commit_id])
            elem.update({'commit_id':commit_id})
        df = pd.DataFrame(dict_a).T.sort_values(by=['time_stamp'],ascending=False)
        save_path = os.path.join(conf.local_path+'defect_features/report',project+'_one.csv')
        columns_write = ['is_fix', 'ns', 'nd', 'nf', 'entropy', 'la', 'ld', 'lt', 'ndev', 'age', 'nuc', 'exp', 'rexp',
                         'sexp', 'contains_bug']
        df.to_csv(save_path,index=False,columns=columns_write)





    def featureCombination(self):
        for p in conf.projects:
            self.loadjson_v(p)
            self.loadjson_a(p)
            self.combination(p)

    def one_featureCombination(self,p):
        #for p in conf.projects:
            self.loadjson_v_one(p)
            self.loadjson_a_one(p)
            self.combination_one(p)

# if __name__=='__main__':
#     # projects_v = ['deeplearning4j']
#     # for project in projects_v:
#     #     load_data_v(project)
#     #
#     projects_a = ['druid']
#     for project in projects_a:
#         FeatureCombination().load_data_a(project)

    # projects_combination = ['deeplearning4j']
    #
    # for project in projects_combination:
    #     combination(project)

    # projects_v = ['deeplearning4j']
    # for project in projects_v:
    #     load_one_commit_v(project, 'b5f0ec072f3fd0da566e32f82c0e43ca36553f39')
    #
    # projects_a = ['deeplearning4j']
    # for project in projects_a:
    #     load_one_commit_a(project, 'b5f0ec072f3fd0da566e32f82c0e43ca36553f39')
    #
    # projects_combination = ['deeplearning4j']
    #
    # for project in projects_combination:
    #     combination_one(project)
