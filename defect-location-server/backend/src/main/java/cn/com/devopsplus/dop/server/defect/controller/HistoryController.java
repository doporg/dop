package cn.com.devopsplus.dop.server.defect.controller;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/defect")
public class HistoryController {
    @RequestMapping("/getPredictFile")
    public JSONObject getPredictFile(){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();
        map.put("success",true);
        map.put("data",mapdatalist);
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
    @RequestMapping("/fileContent")
    public JSONObject fileContent(@RequestBody String data){
        JSONObject mapJson=JSONObject.fromObject(data);

        Map<String,Object> map=new HashMap<>();
        List<String> mapdatalist=new ArrayList<>();

        map.put("success",true);
        map.put("data",mapdatalist);
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }

    @RequestMapping("/deletePredictFile")
    public JSONObject deletePredictFile(@RequestBody String data){
        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        map.put("data","nice");
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
    @RequestMapping("/getLocateFile")
    public JSONObject getLocateFile(){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();
        map.put("success",true);
        map.put("data",mapdatalist);
        JSONObject JO = JSONObject.fromObject(map);
        return JO;

    }
    @RequestMapping("/deleteLocateFile")
    public JSONObject deleteLocateFile(@RequestBody String data){
        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        map.put("data","nice");
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }

}

