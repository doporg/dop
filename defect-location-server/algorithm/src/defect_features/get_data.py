import sys
import os

# from defect_features.log_generation import GitLog

def getdata(p,pythonProjectPath):

    os.chdir(pythonProjectPath)
    sys.path.append(pythonProjectPath)
    # print(commithash)
    from defect_features.change_lines import Changelines

    #for p in conf.projects:
    data=Changelines().getChangelines(p)
    print("getChangelines finished")
    print(data)
    return data


if __name__ == '__main__':
    getdata(sys.argv[1], sys.argv[2])
    #2e037bc30841cc1687b4cab509fd7e44c93b5973
    #getdata("druid","D:/JITO/JITO-Identification")