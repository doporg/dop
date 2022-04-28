#-*- coding: utf-8 -*-
from .base import BaseQuery
from defect_features.db.models import *


class CommitMetaQuery(BaseQuery):
    table = CommitMeta


class DiffusionFeaturesQuery(BaseQuery):
    table = DiffusionFeatures


class SizeFeaturesQuery(BaseQuery):
    table = SizeFeatures


class PurposeFeaturesQuery(BaseQuery):
    table = PurposeFeatures


class HistoryFeaturesQuery(BaseQuery):
    table = HistoryFeatures


class ExperienceFeaturesQuery(BaseQuery):
    table = ExperienceFeatures


# this object may not be used in anywhere and comment it will not affect other function
class ProjectQuery:
    """

    """
    def __init__(self, project):
        self.project = project
        self.cms = CommitMetaQuery(project).do_query()
        self.diffusion_features = DiffusionFeaturesQuery(project).do_query()
        self.size_features = SizeFeaturesQuery(project).do_query()
        self.purpose_features = PurposeFeaturesQuery(project).do_query()
        self.history_features = HistoryFeaturesQuery(project).do_query()
        self.exp_features = ExperienceFeaturesQuery(project).do_query()
        self.__cache_end_commit_id = None


    @property
    def end_commit_id(self):
        if self.__cache_end_commit_id is not None:
            return self.__cache_end_commit_id
        commit_id = None
        for pf in self.purpose_features:
            if pf.is_fix:
                commit_id = pf.commit_id
        self.__cache_end_commit_id = commit_id
        return self.__cache_end_commit_id

    def combine(self):
        features_dict = dict()
        for sf in self.size_features:
            features_dict[sf.commit_id] = dict()
            features_dict[sf.commit_id]['la'] = sf.la
            features_dict[sf.commit_id]['ld'] = sf.ld
            features_dict[sf.commit_id]['lt'] = sf.lt
        for df in self.diffusion_features:
            features_dict[df.commit_id]['ns'] = df.ns
            features_dict[df.commit_id]['nd'] = df.nd
            features_dict[df.commit_id]['nf'] = df.nf
            features_dict[df.commit_id]['entropy'] = df.entropy
        for pf in self.purpose_features:
            features_dict[pf.commit_id]['is_fix'] = pf.is_fix
        for hf in self.history_features:
            features_dict[hf.commit_id]['ndev'] = hf.ndev
            features_dict[hf.commit_id]['age'] = hf.age
            features_dict[hf.commit_id]['nuc'] = hf.nuc
        for ef in self.exp_features:
            features_dict[ef.commit_id]['exp'] = ef.exp
            features_dict[ef.commit_id]['rexp'] = ef.rexp
            features_dict[ef.commit_id]['sexp'] = ef.sexp
        ret_list = list()
        for cm in self.cms:
            cm_dict = features_dict[cm.commit_id]
            if len(cm_dict) == 14:
                cm_dict['commit_id'] = cm.commit_id
                ret_list.append(cm_dict)
            if cm.commit_id == self.end_commit_id:
                break
        return ret_list

def features_to_dict(meta,diffusion,size,purpose,history,experience):
    features_dict = dict()
    for elem in meta:
        features_dict[elem.commit_id] = dict()
        features_dict[elem.commit_id]['commit_id'] = elem.commit_id
        features_dict[elem.commit_id]['project'] = elem.project
        features_dict[elem.commit_id]['is_merge'] = elem.is_merge
        features_dict[elem.commit_id]['time_stamp'] = elem.time_stamp
        features_dict[elem.commit_id]['creation_time'] = elem.creation_time
        features_dict[elem.commit_id]['author_email'] = elem.author_email

        features_dict[elem.commit_id]['commit_message'] = elem.commit_message
    for elem in diffusion:
        features_dict[elem.commit_id]['ns'] = elem.ns
        features_dict[elem.commit_id]['nd'] = elem.nd
        features_dict[elem.commit_id]['nf'] = elem.nf
        features_dict[elem.commit_id]['entropy'] = elem.entropy
    for elem in size:
        features_dict[elem.commit_id]['la'] = elem.la
        features_dict[elem.commit_id]['ld'] = elem.ld
        features_dict[elem.commit_id]['lt'] = elem.lt
    for elem in purpose:
        features_dict[elem.commit_id]['is_fix'] = elem.is_fix
        features_dict[elem.commit_id]['classification'] = elem.classification
        features_dict[elem.commit_id]['find_interval'] = elem.find_interval
        features_dict[elem.commit_id]['linked'] = elem.linked
        features_dict[elem.commit_id]['contains_bug'] = elem.contains_bug
        features_dict[elem.commit_id]['fix_by'] = elem.fix_by
        features_dict[elem.commit_id]['fixes'] = elem.fixes
        features_dict[elem.commit_id]['buggy_lines'] = elem.buggy_lines
        features_dict[elem.commit_id]['clean_lines'] = elem.clean_lines
        features_dict[elem.commit_id]['block'] = elem.block
        features_dict[elem.commit_id]['critical'] = elem.critical
        features_dict[elem.commit_id]['major'] = elem.major
        features_dict[elem.commit_id]['block_total'] = elem.block_total
        features_dict[elem.commit_id]['critical_total'] = elem.critical_total
        features_dict[elem.commit_id]['major_total'] = elem.major_total
        features_dict[elem.commit_id]['fix_file_num'] = elem.fix_file_num
        features_dict[elem.commit_id]['rules'] = elem.rules
        features_dict[elem.commit_id][' bug_fix_files'] = elem. bug_fix_files
    for elem in history:
        features_dict[elem.commit_id]['ndev'] = elem.ndev
        features_dict[elem.commit_id]['age'] = elem.age
        features_dict[elem.commit_id]['nuc'] = elem.nuc
    for elem in experience:
        features_dict[elem.commit_id]['exp'] = elem.exp
        features_dict[elem.commit_id]['rexp'] = elem.rexp
        features_dict[elem.commit_id]['sexp'] = elem.sexp
    return features_dict