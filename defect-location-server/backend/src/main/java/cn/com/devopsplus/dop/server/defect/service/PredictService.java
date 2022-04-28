package cn.com.devopsplus.dop.server.defect.service;
import cn.com.devopsplus.dop.server.defect.util.Run;
import cn.com.devopsplus.dop.server.defect.config.PredictConfig;
import cn.com.devopsplus.dop.server.defect.util.CloneUrl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PredictService {
    @Autowired
    PredictConfig predictConfig;
    @Autowired
    CloneUrl cloneUrl;
    @Autowired
    Run run;
    //读取模型
    public JSONObject get(){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();
        try {
            FileReader fr = new FileReader("D:/web/web/src/main/java/com/example/web//service/model.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            int i=1;
            while ((str = bf.readLine()) != null&&str.length()>=1) {
                Map<String,String> mapdata=new HashMap<>();
                String temp[]=str.split(" ");
                mapdata.put("name",temp[0]);
                mapdata.put("project",temp[1]);
                mapdata.put("creattime",temp[2]);
                mapdatalist.add(mapdata);
                i++;
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put("success",true);
        map.put("data",mapdatalist);
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
    public JSONObject train( String giturl,String starttime,String endtime,String modelName){
        Map<String,Object> map=new HashMap<>();
        String projectname=giturl.substring(giturl.lastIndexOf("/")+1,giturl.lastIndexOf("."));
        System.out.println(projectname);
        String projectpath=predictConfig.getProjectDirectory()+projectname;
        System.out.println(projectpath);
        System.out.println(giturl);
        if (starttime.compareTo(endtime)>=0|| !cloneUrl.cloneRepo(projectpath,giturl)){
            System.out.println("false");
            map.put("success",false);
            JSONObject JO = JSONObject.fromObject(map);
            return JO;
        }
        else {

            String result=run.startbuildmodel(projectname,projectpath,predictConfig.getPythonInterpreterPath(),
                    predictConfig.getBuildModelPythonPath(),predictConfig.getPythonProjectPath(),
                    starttime,endtime);
            if (!result.equals("Model Build Success")){
                map.put("success",false);
                JSONObject JO = JSONObject.fromObject(map);
                return JO;
            }
            else{
                try {
                    FileWriter fr = new FileWriter("D:/web/web/src/main/java/com/example/web//service/model.txt",true);
                    BufferedWriter bf = new BufferedWriter(fr);
                    Calendar calendar= Calendar.getInstance();
                    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd,hh:mm");
                    String time=dateFormat.format(calendar.getTime());
                    bf.write(modelName+" "+projectname+" "+time+" "+giturl);
                    bf.newLine();
                    bf.close();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return get();
    }

    public JSONObject delete( String data){
        JSONObject mapJson=JSONObject.fromObject(data);
        String name=String.valueOf(mapJson.get("name"));
        List<String>list=new ArrayList<>();
        try {
            FileReader fr = new FileReader("D:/web/web/src/main/java/com/example/web//service/model.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null&&str.length()>=1) {
                if(!str.substring(0,str.indexOf(" ")).equals(name))
                    list.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(list);
        try {
            FileWriter fr = new FileWriter("D:/web/web/src/main/java/com/example/web//service/model.txt");
            BufferedWriter bf = new BufferedWriter(fr);
            for(int p=0;p<list.size();p++){
                bf.write(list.get(p));
                bf.newLine();
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return get();
    }
    public JSONObject run( String data){
        JSONObject mapJson=JSONObject.fromObject(data);
        String modelNames=String.valueOf(mapJson.get("modelNames"));
        modelNames=modelNames.substring(2,modelNames.length()-2);
        System.out.println(modelNames);
        String projectname="";
        String giturl="";
        try {
            FileReader fr = new FileReader("D:/web/web/src/main/java/com/example/web//service/model.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null&&str.length()>=1) {
                String []strtemp=str.split(" ");
                if(modelNames.equals(strtemp[0])){
                    projectname=strtemp[1];
                    giturl=strtemp[3];
                    break;
                }
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(projectname);
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();
        /*
        String projectpath=predictConfig.getProjectDirectory()+projectname;
        if (!cloneUrl.cloneRepo(projectpath,giturl)){
            map.put("success",false);
            JSONObject JO = JSONObject.fromObject(map);
            return JO;
        }
        */

        Boolean result=run.startpredict(predictConfig.getPythonInterpreterPath(),predictConfig.getPredictModelPythonPath(),
                projectname,predictConfig.getPythonProjectPath());
        System.out.println(result);
        if(!result){
            map.put("success",true);
            map.put("data",false);
        }
        else {
            map.put("success",true);
            run.startlocate(predictConfig.getPythonInterpreterPath(),predictConfig.getPythonProjectPath(),
                    projectname,predictConfig.getLocationModelPythonPath());

            String target=predictConfig.getPythonProjectPath()+"/train/"+projectname+"locationresult.csv";
            try {
                FileReader fr = new FileReader(target);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                int i=1;
                while ((str = bf.readLine()) != null) {
                    Map<String,String> mapdata=new HashMap<>();
                    mapdata.put("id",String.valueOf(i));
                    mapdata.put("defectCode",str.substring(0,str.indexOf(";----")));
                    mapdata.put("defectLocation",str.substring(str.indexOf(";----")+5,str.length()));
                    mapdatalist.add(mapdata);
                    i++;
                }
                map.put("data",mapdatalist);
                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
}
