package cn.com.devopsplus.dop.server.defect.config;

import org.springframework.stereotype.Component;

@Component
public class PredictConfig {
    private final String projectDirectory="D:/";
    private final String pythonInterpreterPath="D:/python/python.exe ";
    private final String pythonProjectPath="D:/JITO/JITO-Identification";
    private final String buildModelPythonPath=pythonProjectPath+"/defect_features/build_model.py ";
    private final String predictModelPythonPath= pythonProjectPath+"/defect_features/run_model.py ";
    private final String locationModelPythonPath= pythonProjectPath+"/defect_features/Corpus.py ";
    private final String sbflPythonPath= pythonProjectPath+"/defect_features/SBFL/location.py ";
    private final String sbflresultPath= pythonProjectPath+"/defect_features/SBFL/result3.csv";

    public String getSbflresultPath() {
        return sbflresultPath;
    }

    public String getSbflPythonPath() {
        return sbflPythonPath;
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public String getPythonInterpreterPath() {
        return pythonInterpreterPath;
    }

    public String getPythonProjectPath() {
        return pythonProjectPath;
    }

    public String getBuildModelPythonPath() {
        return buildModelPythonPath;
    }

    public String getPredictModelPythonPath() {
        return predictModelPythonPath;
    }

    public String getLocationModelPythonPath() {
        return locationModelPythonPath;
    }

}
