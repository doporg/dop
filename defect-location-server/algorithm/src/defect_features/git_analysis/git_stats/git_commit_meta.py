import re
from datetime import datetime
from defect_features.config import conf


class RawGitLog:
    def __init__(self):
        self.id_line = None
        self.parent_line = None
        self.author_line = None
        self.email_line = None
        self.time_stamp_line = None
        self.committer_line = None
        self.committer_email_line = None
        self.commit_msg_lines = None

    def extract_id(self):
        return self.id_line[-40:]

    def extract_parents(self):
        id_str = self.parent_line[8:]
        id_str = id_str.lstrip().rstrip()
        return id_str.split()

    def extract_author(self):
        return self.author_line[8:], self.email_line[14:]

    def extract_committer(self):
        return self.committer_line[11:], self.committer_email_line[17:]

    def extract_date(self):
        time_stamp = int(self.time_stamp_line[12:])
        return time_stamp, datetime.fromtimestamp(timestamp=time_stamp)

    def extract_commit_message(self):
        assert(isinstance(self.commit_msg_lines, list))
        assert(len(self.commit_msg_lines) > 0)
        msg = ''
        for l in self.commit_msg_lines:
            msg += l
            msg += '\n'
        msg = msg.lstrip('\n ').rstrip('\n ')
        return msg

    # add for one commit
    def extract_one_commit_message(self):
        assert (isinstance(self.commit_msg_lines, list))
        assert (len(self.commit_msg_lines) > 0)
        msg = self.commit_msg_lines[0]
        return msg


class RawGitCommitMeta:

    def __init__(self, project):
        self.project = project
        self.commit_id = None
        self.parent = None
        self.author_name = None
        self.author_email = None
        self.committer_name = None
        self.committer_email = None
        self.date = None
        self.time_stamp = None
        self.commit_message = None
        self.is_merge = False
        self.__cache_son = list()

    def from_raw_git_log(self, raw_git_log):
        assert(isinstance(raw_git_log, RawGitLog))
        self.commit_id = raw_git_log.extract_id()
        self.parent = raw_git_log.extract_parents()
        self.is_merge = len(self.parent) > 1
        self.author_name, self.author_email = raw_git_log.extract_author()
        self.committer_name, self.committer_email = raw_git_log.extract_committer()
        self.time_stamp, self.date = raw_git_log.extract_date()
        self.commit_message = raw_git_log.extract_commit_message()

    # add for one commit
    def from_one_raw_git_log(self, raw_git_log):
        assert(isinstance(raw_git_log, RawGitLog))
        self.commit_id = raw_git_log.extract_id()
        self.parent = raw_git_log.extract_parents()
        self.is_merge = len(self.parent) > 1
        self.author_name, self.author_email = raw_git_log.extract_author()
        self.committer_name, self.committer_email = raw_git_log.extract_committer()
        self.time_stamp, self.date = raw_git_log.extract_date()
        self.commit_message = raw_git_log.extract_one_commit_message()
        
    def add_son(self, commit_id):
        self.__cache_son.append(commit_id)

    @property
    def sons(self):
        return self.__cache_son

