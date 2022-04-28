#-*- coding: utf-8 -*-
import json
from .git_commit_features import GitCommitFeatures
from defect_features.git_analysis.analyze_git_logs import retrieve_git_logs
from defect_features.git_analysis.analyze_git_logs import retrieve_git_log
from defect_features.object.features import PurposeFeatures as PurposeFeaturesObj
from classifier.classifier import *
from analyzer.git_commit_linker import *
# from p3c_pmd.pmd import Pmd


class PurposeFeatures(GitCommitFeatures):
    def __init__(self, rgcm):
        super(PurposeFeatures, self).__init__(rgcm)


    def extract(self):
        """
        reconstruct extract()
        """
        # # ignore commit with the same timestamp
        # if self.check_identical_commit():
        #     return None
        is_fix = False
        if self.is_merge == True:
            classification = 'Merge'
        else:
            classifier = Classifier()
            classification = classifier.categorize(self.commit_msg)
            if classification == "Corrective":
                is_fix = True
        linked = False
        contains_bug = False
        fix_by = None
        fixes = None
        block = 0
        critical = 0
        major = 0
        block_total = 0
        critical_total = 0
        major_total = 0
        file_name_stat = self.namestat.file_name_stat
        la = self.stats.added_number
        ld = self.stats.deleted_number
        find_interval = None

        return {
            'project': self.project,
            'commit_id': self.commit_id,
            'time_stamp': self.time_stamp,
            'is_fix': is_fix,
            'classification':classification,
            'linked':linked,
            'contains_bug':contains_bug,
            'fix_by': fix_by,
            'fixes': fixes,
            'bug_fix_files':None,
            'fix_file_num':0,
            'buggy_lines':None,
            'clean_lines':None,
            'block': block,
            'critical': critical,
            'major': major,
            'block_total': block_total,
            'critical_total': critical_total,
            'major_total': major_total,
            'la': la,
            'ld': ld,
            'file_name_stat': file_name_stat,
            'find_interval': find_interval,
            'rules': None
        }

def extract_to_db_obj(project):
    # start get features
    GitCommitFeatures.initialize(project)
    rgcms = retrieve_git_logs(project)

    db_objs = list()
    pf_objs = list()
    corrective_commits = dict() # key: commit_id; value: pf_obj
    all_commits = dict() # key:commit_id; value: pf_obj
    sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
    # get features done
    for rgcm in sorted_rgcms:
        pf = PurposeFeatures(rgcm)
        attr_dict = pf.extract()
        if attr_dict is None:
            continue
        #print("attr_dict:",attr_dict)
        pf_obj = PurposeFeaturesObj(attr_dict)
        pf_objs.append(pf_obj)
        all_commits[pf_obj.commit_id] = pf_obj
        if pf_obj.classification == 'Corrective':
            corrective_commits[pf_obj.commit_id] = pf_obj

    # # link corrective commit to buggy commit. szz algorithm
    try:
        git_commit_linker = GitCommitLinker(project, corrective_commits, all_commits)
        git_commit_linker.link_corrective_commits()
    except Exception as e:
        # print(e)
        raise
    #p3c pmd

    # pmd = Pmd(project, pf_objs)
    # pmd.pmd_main()
    for pf_obj in pf_objs:
        # to json
        if pf_obj.fix_by != None:
            pf_obj.fix_by = json.dumps(pf_obj.fix_by)
        if pf_obj.fixes != None:
            pf_obj.fixes = json.dumps(pf_obj.fixes)
        if pf_obj.buggy_lines != None:
            pf_obj.buggy_lines = json.dumps(pf_obj.buggy_lines)
        if pf_obj.clean_lines != None:
            pf_obj.clean_lines = json.dumps(pf_obj.clean_lines)
        if pf_obj.bug_fix_files != None:
            pf_obj.bug_fix_files = json.dumps(pf_obj.bug_fix_files)
        #change object/features.py PurposeFeatures object to db object

        pf_dict = {'project':getattr(pf_obj, 'project'), 'commit_id':getattr(pf_obj, 'commit_id'), 'time_stamp':getattr(pf_obj, 'time_stamp'),
                  'is_fix':getattr(pf_obj, 'is_fix'),'classification':getattr(pf_obj, 'classification'),'linked':getattr(pf_obj, 'linked'),
                  'contains_bug':getattr(pf_obj, 'contains_bug'),'fix_by':getattr(pf_obj, 'fix_by'),'fixes':getattr(pf_obj, 'fixes'),
                  'buggy_lines':getattr(pf_obj, 'buggy_lines'),'block':getattr(pf_obj, 'block'),'critical':getattr(pf_obj, 'critical'),
                  'major':getattr(pf_obj, 'major'),'block_total':getattr(pf_obj, 'block_total'),'critical_total':getattr(pf_obj, 'critical_total'),
                  'major_total':getattr(pf_obj, 'major_total'),'file_name_stat':getattr(pf_obj, 'file_name_stat'), 'find_interval':getattr(pf_obj, 'find_interval'),
                  'la':getattr(pf_obj, 'la'),'ld':getattr(pf_obj, 'ld'),'fix_file_num':getattr(pf_obj, 'fix_file_num'),
                  'bug_fix_files':getattr(pf_obj, 'bug_fix_files'),'rules':getattr(pf_obj, 'rules'),"clean_lines":getattr(pf_obj, 'clean_lines')}
        db_objs.append(pf_dict)
        # db_objs.append(pf_obj.to_db_obj())

    return db_objs

# add for one commit
def extract_one_to_db_obj(project):
    # start get features
    GitCommitFeatures.initialize(project)
    rgcms = retrieve_git_log(project)

    db_objs = list()
    pf_objs = list()
    corrective_commits = dict() # key: commit_id; value: pf_obj
    all_commits = dict() # key:commit_id; value: pf_obj
    sorted_rgcms = sorted(rgcms, key=lambda x: x.time_stamp)
    # get features done
    for rgcm in sorted_rgcms:
        pf = PurposeFeatures(rgcm)
        attr_dict = pf.extract()
        if attr_dict is None:
            continue
        pf_obj = PurposeFeaturesObj(attr_dict)
        pf_objs.append(pf_obj)
        all_commits[pf_obj.commit_id] = pf_obj
        if pf_obj.classification == 'Corrective':
            corrective_commits[pf_obj.commit_id] = pf_obj
    # # link corrective commit to buggy commit. szz algorithm
    try:
        git_commit_linker = GitCommitLinker(project, corrective_commits, all_commits)
        git_commit_linker.link_corrective_commits()
    except Exception as e:
        print(e)
        raise
    #p3c pmd

    # pmd = Pmd(project, pf_objs)
    # pmd.pmd_main()
    for pf_obj in pf_objs:
        # to json
        if pf_obj.fix_by != None:
            pf_obj.fix_by = json.dumps(pf_obj.fix_by)
        if pf_obj.fixes != None:
            pf_obj.fixes = json.dumps(pf_obj.fixes)
        if pf_obj.buggy_lines != None:
            pf_obj.buggy_lines = json.dumps(pf_obj.buggy_lines)
        if pf_obj.clean_lines != None:
            pf_obj.clean_lines = json.dumps(pf_obj.clean_lines)
        if pf_obj.bug_fix_files != None:
            pf_obj.bug_fix_files = json.dumps(pf_obj.bug_fix_files)
        #change object/features.py PurposeFeatures object to db object
        db_objs.append(pf_obj.to_db_obj())
    return db_objs


if __name__ == '__main__':
    project = 'ant'
    extract_to_db_obj(project)

