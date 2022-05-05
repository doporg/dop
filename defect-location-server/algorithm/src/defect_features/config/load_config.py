#-*- coding: utf-8 -*-
import simplejson
import os
import sys

# os.chdir("/Users/lifeasarain/Desktop/JITO/JIT-Identification")
# sys.path.append("/Users/lifeasarain/Desktop/JITO/JIT-Identification")

class Config:
    def __init__(self):
        #conf = file("../config.json", "r") # when run each dimension
        #conf = file("config.json", "r")  # when run main
        with open("defect_features/config.json","r") as conf:
            conf_str = conf.read()
            conf_json = simplejson.loads(conf_str)
            self.data_path = conf_json['data_root_path']
            self.git_log_path = conf_json['git_log_path']
            self.projects = conf_json['projects']
            # self.sql_config = SQLConfig(conf_json['sql'])
            self.consider_extensions = set(conf_json['extensions'])
            self.df_basic_path = conf_json['df_basic_path']
            self.local_path = conf_json['local_path']
            self.modelName = conf_json['modelName']

    def updata(self):
        with open("defect_features/config.json","r",encoding='utf-8') as conf:
            conf_str = conf.read()
            conf_json = simplejson.loads(conf_str)
            self.data_path = conf_json['data_root_path']
            self.git_log_path = conf_json['git_log_path']
            self.projects = conf_json['projects']
            # self.sql_config = SQLConfig(conf_json['sql'])
            self.consider_extensions = set(conf_json['extensions'])
            self.df_basic_path = conf_json['df_basic_path']
            self.local_path = conf_json['local_path']
            self.modelName = conf_json['modelName']

    def project_log_path(self, project_name, log_filename=None):
        #assert(project_name in self.projects)
        path = os.path.join(self.git_log_path, project_name)
        if not os.path.exists(path):
            os.makedirs(path)
        if log_filename is None:
            return path
        else:
            return os.path.join(path, log_filename + '.log')


    def project_path(self, project_name):
        #assert(project_name in self.projects)
        return os.path.join(self.data_path, project_name)
        # return (self.data_path+project_name)

    def local_path(self):
        return (self.local_path)



# class SQLConfig:
#     def __init__(self, sql_json):
#         self.vendor = sql_json['vendor']
#         self.dbname = sql_json['dbname']
#         self.username = sql_json['username']
#         self.password = sql_json['password']
#         self.ip = sql_json['ip']

