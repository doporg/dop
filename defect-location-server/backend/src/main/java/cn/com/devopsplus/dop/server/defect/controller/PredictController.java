package cn.com.devopsplus.dop.server.defect.controller;

import cn.com.devopsplus.dop.server.defect.service.PredictService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/defect")
public class PredictController {
    @Autowired
    PredictService predictService;

    @RequestMapping("/get")
    public JSONObject get(){
        return predictService.get();
    }

    @RequestMapping("/add")
    public JSONObject add(@RequestBody String data){

        JSONObject mapJson=JSONObject.fromObject(data);
        System.out.println(mapJson);
        String modelName=String.valueOf(mapJson.get("modelName"));
        String giturl=String.valueOf(mapJson.get("gitBranch"));
        String starttime=String.valueOf(mapJson.get("startTime")).substring(0,10);
        String endtime=String.valueOf(mapJson.get("endTime")).substring(0,10);

        return predictService.train(giturl,starttime,endtime,modelName);

    }
    @RequestMapping("/delete")
    public JSONObject delete(@RequestBody String data){
        return predictService.delete(data);
    }
    @RequestMapping("/getTrainData")//no
    public JSONObject getTrainData(@RequestBody String data){
        JSONObject mapJson=JSONObject.fromObject(data);
        System.out.println(mapJson);

        Map<String,Object> map=new HashMap<>();
        List<String> mapdatalist=new ArrayList<>();
        //mapdatalist.add("diff --git a/druid-admin/src/main/java/com/alibaba/druid/admin/servlet/MonitorViewServlet.java b/druid-admin/src/main/java/com/alibaba/druid/admin/servlet/MonitorViewServlet.java");
        map.put("success",true);
        map.put("data",mapdatalist);
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
    @RequestMapping("/run")
    public JSONObject run(@RequestBody String data){
        return predictService.run(data);
    }
}
