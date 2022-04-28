package cn.com.devopsplus.dop.server.defect.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Run {
    public String startbuildmodel(String projectName,String projectPath,String pythonenv,String pythonpath,
                                  String pythonProjectPath,String startTime,String endTime){
        String result = null;

        try {
            //根目录
            String dataPath = projectPath.substring(0, projectPath.length() - projectName.length());
            String pythonArgs = pythonenv + pythonpath + projectName + " " + pythonProjectPath + " " + dataPath + " " + startTime + " " + endTime;
            //String pythonArgs = pythonenv +"D:/JITO/JITO-Identification/defect_features/lianxi.py ";
            System.out.println(pythonArgs);

            Process proc = Runtime.getRuntime().exec(pythonArgs);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            for(String line = null; (line = in.readLine()) != null; result = line) {
            }
        } catch (IOException var14) {
            var14.printStackTrace();
        }
        return result;

    }
    public Boolean startpredict(String pythonenv,String pythonPath,String projectname,String pythonProjectPath) {
        String result = null;
        try {
            String pythonArgs = pythonenv + pythonPath + projectname + " " + pythonProjectPath;
            Process proc = Runtime.getRuntime().exec(pythonArgs);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            for(String line = null; (line = in.readLine()) != null; result = line) {
            }
            in.close();
            proc.waitFor();
        } catch (IOException var10) {
            var10.printStackTrace();
        } catch (InterruptedException var11) {
            var11.printStackTrace();
        }
        return result.equals("1");
    }
    public String startlocate(String pythonenv,String pythonProjectPath,String projectname,String pythonpath3) {
        String result = null;
        try {

            String pythonArgs = pythonenv + pythonpath3 + projectname + " " + pythonProjectPath;
            Process proc = Runtime.getRuntime().exec(pythonArgs);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            for(String line = null; (line = in.readLine()) != null; result = line) {
            }
            in.close();
            proc.waitFor();
        } catch (IOException var10) {
            var10.printStackTrace();
        } catch (InterruptedException var11) {
            var11.printStackTrace();
        }
        return result;
    }
    public String sbfllocate(String pythonenv,String path,String pythonpath4,String pythonProjectPath) {
        String result = null;
        try {

            String pythonArgs = pythonenv + pythonpath4 + path + " " + pythonProjectPath;
            System.out.println(pythonArgs);
            Process proc = Runtime.getRuntime().exec(pythonArgs);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            for(String line = null; (line = in.readLine()) != null; result = line) {
            }
            in.close();
            proc.waitFor();
        } catch (IOException var10) {
            var10.printStackTrace();
        } catch (InterruptedException var11) {
            var11.printStackTrace();
        }
        return result;
    }
}
