package cn.com.devopsplus.dop.server.defect.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PredictConfig {
    private final String AlgorithmAddress="http://127.0.0.1:5000";

    private final String dataPath="D:/Fee/";

    private final String pythonProjectPath="D:/JITO/JITO-Identification";

    private final String buildModelPythonPath=AlgorithmAddress+"/build_model";

    private final String trainDataPythonPath=AlgorithmAddress+"/get_data";

    private final String runModelPythonPath= AlgorithmAddress+"/run_model";

    private final String locationModelPythonPath= AlgorithmAddress+"/corpus";

    private final String prflPythonPath= AlgorithmAddress+"/locate";

    private final String prflFilePath= "D:/JITO/JITO-Identification/defect_features/PRFL/test.txt";

}