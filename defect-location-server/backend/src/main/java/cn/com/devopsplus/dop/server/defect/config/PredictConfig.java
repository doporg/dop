package cn.com.devopsplus.dop.server.defect.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PredictConfig {
    private final String AlgorithmAddress="http://127.0.0.1:5005";

    private final String dataPath="/tmp/";

    private final String pythonProjectPath="/code/src";

    private final String buildModelPythonPath=AlgorithmAddress+"/build_model";

    private final String trainDataPythonPath=AlgorithmAddress+"/get_data";

    private final String runModelPythonPath= AlgorithmAddress+"/run_model";

    private final String locationModelPythonPath= AlgorithmAddress+"/corpus";

    private final String prflPythonPath= AlgorithmAddress+"/locate";

    private final String prflFilePath= dataPath+"test.txt";

}