package cn.com.devopsplus.dop.server.defect.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class Run {
    public String startbuildmodel(String buildModelPythonPath,String projectName, String pythonProjectPath,
                                  String dataPath,String startTime,String endTime,String modelName){
        String result = "false";

        try {
            URL url = new URL(buildModelPythonPath + "?" + "projectName=" + projectName +
                    "&pythonProjectPath=" + pythonProjectPath + "&dataPath="+dataPath +
                    "&start_time="+startTime+"&end_time="+endTime+"&modelName="+modelName);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");

            if (conn.getResponseCode() != 200) {
                return result;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            result= br.readLine();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;

    }
    public String getTrainData(String trainDataPythonPath,String projectName, String pythonProjectPath){
        String result = "false";

        try {
            URL url = new URL(trainDataPythonPath + "?" + "projectName=" + projectName +
                    "&pythonProjectPath=" + pythonProjectPath );

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");

            if (conn.getResponseCode() != 200) {
                return result;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            result= br.readLine();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;

    }
    public Boolean startpredict(String runModelPythonPath,String predict_project,String pythonProjectPath,String model_project,String model_name) {
        String result = null;
        try {
            URL url = new URL(runModelPythonPath + "?" + "predict_project=" + predict_project +
                    "&pythonProjectPath=" + pythonProjectPath + "&model_project=" + model_project + "&model_name=" +model_name);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            result= br.readLine();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result.equals("1");
    }
    public String startlocate(String locationModelPythonPath,String locationprojectname,String projectName,String pythonProjectPath,String modelName) {
        String result = null;
        try {
            URL url = new URL(locationModelPythonPath + "?" + "locationprojectname=" + locationprojectname +
                    "&projectName=" + projectName + "&pythonProjectPath=" + pythonProjectPath + "&modelName=" +modelName);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            result= br.readLine();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
    public String prfllocate(String prflPythonPath,String testCoverage,String method,String projectpath) {
        String result = null;
        try {
            URL url = new URL(prflPythonPath + "?" + "testCoverage=" + testCoverage +
                    "&method=" + method + "&projectpath=" + projectpath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            result = br.readLine();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
