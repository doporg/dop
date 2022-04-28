#-*- coding: utf-8 -*-
from sqlalchemy import Column, String, Integer, Boolean, DateTime, Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.dialects.mysql import TEXT

Base = declarative_base()

###################################################################
#
# Models defining the Mysql tables used to store information
#
##################################################################
MAX_LENGTH = 10240

class CommitMeta(Base):
    """
    Currently, we only have the ground-truth labels of revisions
    in SVN repositories. A problem hindering the our study is that when
    using SVN commands, you must connect to network and interactm
    with servers managing the repositories. It really slows down the
    progress of our study.

    Luckily, SVN revisions are corresponding to the git commits in the git
    repository, which gives us an opportunity to directly deal with
    git commits.
    """
    __tablename__ = 'commit_meta'

    id = Column(Integer, primary_key=True, autoincrement=True)
    project = Column(String(63), nullable=False)
    commit_id = Column(String(40), nullable=False)
    is_merge = Column(Boolean, nullable=False)
    time_stamp = Column(Integer, nullable=False)
    creation_time = Column(DateTime, nullable=False)
    author_email = Column(String(128),default=None)
    commit_message = Column(String(MAX_LENGTH), default=None)


class DiffusionFeatures(Base):
    __tablename__ = 'diffusion_features'
    id = Column(Integer, primary_key=True, autoincrement=True)
    project = Column(String(63), nullable=False)
    commit_id = Column(String(63), nullable=False)
    ns = Column(Integer, nullable=False)
    nd = Column(Integer, nullable=False)
    nf = Column(Integer, nullable=False)
    entropy = Column(Float, nullable=False)


class SizeFeatures(Base):
    __tablename__ = 'size_features1'
    id = Column(Integer, primary_key=True, autoincrement=True)
    project = Column(String(63), nullable=False)
    commit_id = Column(String(63), nullable=False)
    la = Column(Integer, nullable=False)
    ld = Column(Integer, nullable=False)
    lt = Column(Float, nullable=False)


class PurposeFeatures(Base):
    __tablename__ = 'purpose_features'
    id = Column(Integer, primary_key=True, autoincrement=True)
    project = Column(String(63), nullable=False)
    commit_id = Column(String(63), nullable=False)
    time_stamp = Column(Integer, nullable=False)
    is_fix = Column(Integer, nullable=False)
    classification = Column(String(32), nullable=True)
    linked = Column(Boolean,default=False)
    contains_bug = Column(Boolean,default=False)
    fix_by = Column(TEXT(MAX_LENGTH), default=None) # fix by which commit or commits
    find_interval = Column(Integer,default=None) #
    fixes = Column(TEXT(MAX_LENGTH), default=None) # corrective commit fix which commit or commits
    bug_fix_files = Column(TEXT(MAX_LENGTH), default=None) # bug_fix file 列表
    fix_file_num = Column(Integer,default=0) 
    buggy_lines = Column(TEXT(MAX_LENGTH),default=None) # data type {file1:[line1,line2],file2:[line1,line2]...}
    clean_lines = Column(TEXT(MAX_LENGTH), default=None)
    block = Column(Integer,default=0) # p3c-pmd warning number of priority 1
    critical = Column(Integer,default=0) # p3c-pmd warning number of priority 2
    major = Column(Integer,default=0) # p3c-pmd warning number of priority 3
    block_total = Column(Integer,default=0)
    critical_total = Column(Integer, default=0)
    major_total = Column(Integer, default=0)
    rules = Column(TEXT(MAX_LENGTH),default=None) # rules break


class HistoryFeatures(Base):
    __tablename__ = 'history_features'
    id = Column(Integer, primary_key=True, autoincrement=True)
    project = Column(String(63), nullable=False)
    commit_id = Column(String(63), nullable=False)
    ndev = Column(Float, nullable=False)
    age = Column(Float, nullable=False)
    nuc = Column(Float, nullable=False)


class ExperienceFeatures(Base):
    __tablename__ = 'experience_features'

    id = Column(Integer, primary_key=True, autoincrement=True)
    project = Column(String(63), nullable=False)
    commit_id = Column(String(63), nullable=False)
    exp = Column(Integer, nullable=False)
    rexp = Column(Float, nullable=False)
    sexp = Column(Float, nullable=False)


table_map = {
    'commit_meta': CommitMeta,
    'diffusion_features': DiffusionFeatures,
    'size_features1': SizeFeatures,
    'purpose_features': PurposeFeatures,
    'history_features': HistoryFeatures,
    'experience_features': ExperienceFeatures
}
