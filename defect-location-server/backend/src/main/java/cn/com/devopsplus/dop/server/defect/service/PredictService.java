package cn.com.devopsplus.dop.server.defect.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.com.devopsplus.dop.server.defect.config.PredictConfig;
import cn.com.devopsplus.dop.server.defect.mapper.ModelMapper;
import cn.com.devopsplus.dop.server.defect.pojo.Model;
import cn.com.devopsplus.dop.server.defect.util.CloneUrl;
import cn.com.devopsplus.dop.server.defect.util.Run;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PredictService {
    @Autowired
    PredictConfig predictConfig;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CloneUrl cloneUrl;
    @Autowired
    Run run;
    //读取模型
    public JSONObject get(Integer userId){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();

        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Model> models = modelMapper.selectList(queryWrapper);
        //models.forEach(System.out::println);

        for (int i =0;i<models.size();i++){
            Map<String,String> mapdata=new HashMap<>();
            mapdata.put("name",models.get(i).getModelName());
            mapdata.put("project",models.get(i).getModelProject());
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time=dateFormat.format(models.get(i).getCreateTime());
            mapdata.put("creattime", time);
            mapdatalist.add(mapdata);
        }
        map.put("success",true);
        map.put("data",mapdatalist);
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
    public JSONObject train( String giturl,String starttime,String endtime,String modelName,Integer userId){
        Map<String,Object> map=new HashMap<>();
        String projectname=giturl.substring(giturl.lastIndexOf("/")+1,giturl.lastIndexOf("."));
        String projectpath=predictConfig.getDataPath()+projectname;
        //克隆代码仓库
        if (starttime.compareTo(endtime)>=0|| !cloneUrl.cloneRepo(projectpath,giturl)){
            System.out.println("clone false");
            map.put("success",false);
            JSONObject JO = JSONObject.fromObject(map);
            return JO;
        }
        else {
            //构建模型
            System.out.println("train begin");
            String result=run.startbuildmodel(predictConfig.getBuildModelPythonPath(),projectname,predictConfig.getPythonProjectPath(),
                    predictConfig.getDataPath(), starttime,endtime,modelName);
            System.out.println("train over");
            //失败
            if (!result.equals("Model Build Success")){
                System.out.println("train fail");
                map.put("success",false);
                JSONObject JO = JSONObject.fromObject(map);
                return JO;
            }
            //成功
            else{
                System.out.println("train success");
                Calendar calendar= Calendar.getInstance();
                Model model=new Model();
                model.setModelName(modelName);
                model.setModelProject(projectname);
                model.setCreateTime(calendar.getTime());
                model.setGitUrl(giturl);
                model.setUserId(userId);
                modelMapper.insert(model);
            }

        }
        return get(userId);
    }

    public JSONObject delete( String data,Integer userId){
        JSONObject mapJson=JSONObject.fromObject(data);
        String modelName=String.valueOf(mapJson.get("name"));
        Model model=new Model();
        model.setModelName(modelName);
        model.setUserId(userId);
        modelMapper.deleteByMultiId(model);
        return get(userId);
    }
    public JSONObject getTrainData( String data){
        JSONObject mapJson=JSONObject.fromObject(data);
        String giturl=String.valueOf(mapJson.get("gitBranch"));
        Map<String,Object> map=new HashMap<>();
        List mapdatalist;
        String projectname=giturl.substring(giturl.lastIndexOf("/")+1,giturl.lastIndexOf("."));
        String projectpath=predictConfig.getDataPath()+projectname;

        //克隆代码仓库
        if (!cloneUrl.cloneRepo(projectpath,giturl)){
            System.out.println("clone false");
            map.put("success",false);
            JSONObject JO = JSONObject.fromObject(map);
            return JO;
        }
        else {
            String result=run.getTrainData(predictConfig.getTrainDataPythonPath(),projectname,predictConfig.getPythonProjectPath());
            if(result.equals("false")){
                map.put("success",false);
                JSONObject JO = JSONObject.fromObject(map);
                return JO;
            }
            else {
                JSONArray entryArray = JSONArray.fromObject(result);
                mapdatalist=JSONArray.toList(entryArray, String.class);
                map.put("success",true);
                map.put("data",mapdatalist);
                JSONObject JO = JSONObject.fromObject(map);
                return JO;
            }
        }
    }
    public JSONObject run( String data,Integer userId){
        JSONObject mapJson=JSONObject.fromObject(data);

        String model_name=String.valueOf(mapJson.get("modelNames"));
        model_name=model_name.substring(2,model_name.length()-2);

        String predict_url=String.valueOf(mapJson.get("gitBranch"));
        String predict_project=predict_url.substring(predict_url.lastIndexOf("/")+1,predict_url.lastIndexOf("."));

        Model model=new Model();
        model.setModelName(model_name);
        model.setUserId(userId);
        Model Entity=modelMapper.selectByMultiId(model);
        String model_project=Entity.getModelProject();

        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();

        String projectpath=predictConfig.getDataPath()+predict_project;
        if (!cloneUrl.cloneRepo(projectpath,predict_url)){
            map.put("success",false);
            JSONObject JO = JSONObject.fromObject(map);
            return JO;
        }
        //预测
        Boolean result=run.startpredict(predictConfig.getRunModelPythonPath(),predict_project,predictConfig.getPythonProjectPath(),model_project,model_name);
        System.out.println(result);
        //无缺陷
        if(!result){
            map.put("success",true);
            map.put("data",false);
        }
        //有缺陷
        else {
            map.put("success",true);
            String res=run.startlocate(predictConfig.getLocationModelPythonPath(),predict_project,model_project,predictConfig.getPythonProjectPath(),model_name);
            System.out.println(res);

            JSONArray entryArray = JSONArray.fromObject(res);
            List datalist=JSONArray.toList(entryArray, String.class);

            String str;
            int i=0;
            while (i<datalist.size()) {
                str= (String) datalist.get(i);
                Map<String,String> mapdata=new HashMap<>();
                mapdata.put("id",String.valueOf(i+1));
                mapdata.put("defectCode",str.substring(0,str.indexOf("----")));
                mapdata.put("defectLocation",str.substring(str.indexOf("----")+4,str.length()));
                mapdatalist.add(mapdata);
                i++;
            }
            map.put("data",mapdatalist);
        }
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
}
