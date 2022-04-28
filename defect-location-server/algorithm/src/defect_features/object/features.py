#-*- coding: utf-8 -*-
from defect_features.object import BaseObj


class Features(BaseObj):
    table_name = ''
    attributes = []

    def __init__(self, attr_dict=None):
        if attr_dict is not None:
            super(Features, self).__init__(attr_dict, True)
        else:
            super(Features,self).__init__()


class DiffusionFeatures(Features):
    table_name = 'diffusion_features'
    attributes = ['project', 'commit_id', 'ns', 'nd', 'nf', 'entropy']


class SizeFeatures(Features):
    table_name = 'size_features1'
    attributes = ['project', 'commit_id', 'la', 'ld', 'lt']


class PurposeFeatures(Features):
    table_name = 'purpose_features'
    attributes = ['project', 'commit_id', 'time_stamp','is_fix','classification','linked',
                  'contains_bug','fix_by','fixes','buggy_lines','block','critical','major','block_total',
                  'critical_total','major_total','file_name_stat', 'find_interval','la','ld','fix_file_num',
                  'bug_fix_files','rules',"clean_lines"]


class HistoryFeatures(Features):
    table_name = 'history_features'
    attributes = ['project', 'commit_id', 'ndev', 'age', 'nuc']


class ExperienceFeatures(Features):
    table_name = 'experience_features'
    attributes = ['project', 'commit_id', 'exp', 'rexp', 'sexp']

