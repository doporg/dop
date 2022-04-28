import os
import sys
import shutil
import psutil
from copy import deepcopy
from marshal import dump,load
from defect_features.config import conf
# os.chdir("/Users/lifeasarain/Desktop/JITO/JIT-Identification/")


class MemManager(object):

    """
    由于GitCommitFeatures类的变量parent_file_stats占用内存过大，而且绝大多少数据在短时间内又不再使用，所以需要将这部分数据
    以文件的形式转存到硬盘中。由于python内存管理器并不能很好的完成这个工作，所以定义类这个类来管理parent_file_stats。
    """
    # 当系统内存占用达到 MAX_PER时，调度模块开始启动;如果程序开始时，系统内存占用就以超过MAX_PER，则程序无法运行，所以不要MAX_PER设置过低。
    # 当parent_file_stats被访问时，计数器重置为RESET_COUNER
    RESET_COUNTER = 1000
    MAX_PER = 70
    MIN_IN = 10
    def __init__(self,project):
        """
        :param project:
        """
        # self.project = project
        # if not os.path.isdir("../data/" + self.project):
        #     os.mkdir("../data/" + self.project)
        # else:
        #     shutil.rmtree("../data/" + self.project)
        #     os.mkdir("../data/" + self.project)
        # if not os.path.isdir("../data/" + self.project + "/parent_file_stats"):
        #     os.mkdir("../data/" + self.project + "/parent_file_stats")
        # self.basic_path = "../data/" + self.project + "/parent_file_stats/"
        # self.in_mem = dict()
        # self.counter_dict = dict()

        self.project = project
        if not os.path.isdir(conf.local_path + "data/" + self.project):
            os.mkdir(conf.local_path + "data/" + self.project)
        else:
            shutil.rmtree(conf.local_path + "data/" + self.project)
            os.mkdir(conf.local_path + "data/" + self.project)
        if not os.path.isdir(conf.local_path + "data/" + self.project + "/parent_file_stats"):
            os.mkdir(conf.local_path + "data/" + self.project + "/parent_file_stats")
        self.basic_path = conf.local_path + "data/" + self.project + "/parent_file_stats/"
        self.in_mem = dict()
        self.counter_dict = dict()


    def update_counter(self,commit_id):
        tmp_dict = dict()
        for key, value in self.counter_dict.items():
            if key == commit_id:
                tmp_dict[key] = MemManager.RESET_COUNTER
            else:
                tmp_dict[key] = value - 1
        self.counter_dict = tmp_dict

    def put(self,commit_id,stats):
        # 内存使用过大时，选择若干parent_file_stats从内存调出,直到符合要求
        self.pop()
        self.in_mem[commit_id] = stats
        # deepcopy_stats 需要重置counter
        self.counter_dict[commit_id] = MemManager.RESET_COUNTER

    # 转存到硬盘中，以节约内存
    def pop(self):
        while psutil.virtual_memory().percent > MemManager.MAX_PER:
            if len(self.in_mem) < MemManager.MIN_IN:
                break
            # 选择counter最小的commit
            id_remove = min(self.counter_dict.items(), key=lambda x: x[1])[0]
            with open(self.basic_path+id_remove,'wb') as file:
                stats_remove = self.in_mem[id_remove]
                dump(stats_remove,file)
            del self.counter_dict[id_remove]
            del self.in_mem[id_remove]

    # 浅复制
    def copy_stats(self,parent_id,commit_id):
        tmp = self.in_mem[parent_id]
        self.in_mem[commit_id] = tmp
        del self.in_mem[parent_id]
        del self.counter_dict[parent_id]
        self.counter_dict[commit_id] = MemManager.RESET_COUNTER

    # 深复制
    def deepcopy_stats(self,parent_id,commit_id):
        stats_new = deepcopy(self.in_mem[parent_id])
        self.put(commit_id,stats_new)

    # commit第一次出现，且无法直接承接 parent commit的 parent_file_stats时，需特殊处理
    # 主要是 没有parent_commit和merge_commit两种情况。
    def debut(self,commit_id):
        stats = dict()
        self.put(commit_id,stats)

    def get(self,commit_id):
        if commit_id in self.in_mem:
            stats_target = self.in_mem[commit_id]
        else:# 不在内存中，到硬盘中查找
            # 文件不存在，报错
            if not os.path.isfile(self.basic_path+commit_id):
                raise FileNotFoundError(commit_id)
            else:
                with open(self.basic_path+commit_id,'rb') as file:
                    stats_target = load(file)
                self.put(commit_id,stats_target)
        return stats_target

