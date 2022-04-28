#-*- coding: utf-8 -*-
from defect_features.db.api import DbAPI


class BaseQuery(object):
    table = None

    def __init__(self, project=None):
        self.db_api = DbAPI()
        self.fields = None
        self.query_results = None
        self.project = project

    def initialize_fields(self, fields):
        self.fields = set()
        for f in fields:
            self.fields.add(f)

    def do_query(self):

        self.query_results = self.db_api.retrieve_query(self.table,
                                                        self.project)

        if self.query_results is None:
            return None
        else:
            return self.query_results

    def __del__(self):
        self.db_api.close_session()