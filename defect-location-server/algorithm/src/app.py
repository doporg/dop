from flask import Flask,request
from flask_cors import *

app = Flask(__name__)
CORS(app, supports_credentials=True)

@app.route("/run_model", methods=['GET'])
def run_my_model():
    # if __name__ == '__main__':
    #      runmodel(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
    #      #2e037bc30841cc1687b4cab509fd7e44c93b5973
    #      #runmodel("Feeder","D:/JITO/JITO-Identification","druid","A")

    from defect_features import run_model
    predict_project = request.args['predict_project']
    pythonProjectPath = request.args['pythonProjectPath']
    model_project = request.args['model_project']
    model_name = request.args['model_name']
    ret = run_model.runmodel(predict_project, pythonProjectPath, model_project, model_name)
    return ret

@app.route("/build_model")
def build_my_model():
    # if __name__ == '__main__':
    #     #buildmodel(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6])
    #     # buildmodel("druid", "/Users/lifeasarain/Desktop/JITO/JIT-Identification", "/Users/lifeasarain/IdeaProjects/")
    #     buildmodel("Feeder", "/code/JITO-Identification", "/usr/project/", "2015-1-1", "2016-9-1","FFFF")
    # def buildmodel(projectName, pythonProjectPath, dataPath, start_time, end_time, modelName):
    from defect_features import build_model
    projectName = request.args['projectName']
    pythonProjectPath = request.args['pythonProjectPath']
    dataPath = request.args['dataPath']
    start_time = request.args['start_time']
    end_time = request.args['end_time']
    modelName = request.args['modelName']
    build_model.buildmodel(projectName, pythonProjectPath, dataPath, start_time, end_time, modelName)
    return 'Model Build Success'

@app.route("/corpus")
def my_corpus():
    # if __name__ == '__main__':
#     corpus(sys.argv[1],sys.argv[2],sys.argv[3])
#     #corpus("Feeder","D:/JITO/JITO-Identification","FFFF")
#     def corpus(projectname, pythonpath, modelName):
    from defect_features import Corpus
    locationprojectname=request.args['locationprojectname']
    projectName = request.args['projectName']
    pythonProjectPath = request.args['pythonProjectPath']
    modelName = request.args['modelName']
    res=Corpus.corpus(locationprojectname,pythonProjectPath,projectName,modelName)
    return str(res)

@app.route("/locate")
def my_locate():
    # if __name__ == '__main__':
    #     corpus(sys.argv[1],sys.argv[2],sys.argv[3])
    #     #corpus("Feeder","D:/JITO/JITO-Identification","FFFF")
    #     def corpus(projectname, pythonpath, modelName):
    from defect_features.PRFL import prfl
    testCoverage = request.args['testCoverage']
    method = request.args['method']
    projectpath = request.args['projectpath']
    res=prfl.locate(testCoverage,method,projectpath)
    return str(res)

@app.route("/get_data")
def my_get_data():
    # if __name__ == '__main__':
    #     corpus(sys.argv[1],sys.argv[2],sys.argv[3])
    #     #corpus("Feeder","D:/JITO/JITO-Identification","FFFF")
    #     def corpus(projectname, pythonpath, modelName):
    from defect_features import get_data
    projectName = request.args['projectName']
    pythonProjectPath = request.args['pythonProjectPath']
    res=get_data.getdata(projectName, pythonProjectPath)
    return str(res)

if __name__ == '__main__':
    app.run(host="0.0.0.0",port=5005)
