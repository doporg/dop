#-*- coding: utf-8 -*-
from defect_features.db.models import table_map


class BaseObj(object):

    table_name = ''
    attributes = []

# initialize commit_meta purpose_features and other features object .
    #  We can get all features then construct a commit object to store all this features
    def __init__(self, attr_dict=None, initialize=False):
        if initialize:
            assert(isinstance(attr_dict, dict))
            for attr in self.attributes:
                setattr(self, attr, attr_dict[attr])

    def to_db_obj(self):
        try:
            db_class = table_map[self.table_name]
            db_obj = db_class()
            for attr in self.attributes:
                assert(hasattr(self, attr))
                setattr(db_obj, attr, getattr(self, attr))
            # db_obj type <class 'defect_features.db.models.CommitMeta'>
            # db_obj type <class 'defect_features.db.models.DiffusionFeatures'>
            # db_obj type <class 'defect_features.db.models.ExperienceFeatures'>
            # and so on
            return db_obj
        except KeyError:
            raise

    def from_db_obj(self, db_obj):
        for attr in self.attributes:
            if hasattr(db_obj, attr):
                attr_value = getattr(db_obj, attr)
                setattr(self, attr, attr_value)
            else:
                setattr(self, attr, None)

    def print_attributes(self):
        for attr in self.attributes:
            print (attr+':', getattr(self, attr),)
        print ('')





