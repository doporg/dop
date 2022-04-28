#-*- coding: utf-8 -*-
from defect_features.git_analysis.git_stats.git_commit_meta import RawGitCommitMeta
from defect_features.object import BaseObj


class CommitMeta(BaseObj):
    table_name = "commit_meta"

    # attributes = ['project', 'commit_id',
    #               'is_merge', 'time_stamp', 'creation_time','author_email','commit_message']

    attributes = ['project', 'commit_id',
                  'is_merge', 'time_stamp', 'creation_time', 'author_email']

    def from_git_log(self, rgc):
        assert (isinstance(rgc, RawGitCommitMeta))
        setattr(self, 'project', rgc.project)
        setattr(self, 'commit_id', rgc.commit_id)
        setattr(self, 'is_merge', rgc.is_merge)
        setattr(self, 'time_stamp', rgc.time_stamp)
        setattr(self, 'creation_time', rgc.date)
        setattr(self,'author_email',rgc.author_email)
        # setattr(self,'commit_message',rgc.commit_message)
