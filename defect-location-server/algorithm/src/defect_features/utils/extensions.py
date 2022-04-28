#-*- coding: utf-8 -*-
import os
from defect_features.config import conf


def in_our_extensions(path):
    if len(conf.consider_extensions) == 0:
        return True
    ext = os.path.splitext(path)[1]
    return ext in conf.consider_extensions
