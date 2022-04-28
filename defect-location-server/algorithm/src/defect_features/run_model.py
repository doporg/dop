import sys
import os

# from defect_features.log_generation import GitLog

def runmodel(p,pythonProjectPath,projectName,modelName):

    os.chdir(pythonProjectPath)
    sys.path.append(pythonProjectPath)

    # print(commithash)

    from defect_features.config import conf
    from defect_features.onelog_generation import GitOneLog
    from defect_features.onelog_feature import OneLogFeatures
    from defect_features.utils.features_combination import FeatureCombination
    from defect_features.idmodel import Idmodel
    from defect_features.change_lines import Changelines

    #for p in conf.projects:
    print('Project',p)
    GitOneLog().commitrun(p)
    print("gitlog finished")
    OneLogFeatures().log_one_feature(p)
    print("log features finished")
    FeatureCombination().one_featureCombination(p)
    print("featuresCombination finished")
    Changelines().getChangelines(p)
    print("getChangelines finished")
    result = Idmodel().runIdmodel(pythonProjectPath,p,projectName,modelName)
    if(1 == result):
        print('1')
        return '1'
    else:
        print('0')
        return '0'


if __name__ == '__main__':
     runmodel(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
     #2e037bc30841cc1687b4cab509fd7e44c93b5973
     #runmodel("Feeder","D:/JITO/JITO-Identification","druid","A")