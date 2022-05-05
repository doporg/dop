package cn.com.devopsplus.dop.server.defect.controller;

import cn.com.devopsplus.dop.server.defect.service.PredictService;
import cn.com.devopsplus.dop.server.defect.util.GetUserId;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@RequestMapping("/defect")
public class PredictController {
    @Autowired
    PredictService predictService;
    @Autowired
    GetUserId getUserId;
    @RequestMapping("/get")
    public JSONObject get(){
        return predictService.get(getUserId.getUserId());
    }

    @ApiOperation(value = "添加模型", notes = "根据用户id添加模型信息")
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String data){
        JSONObject mapJson=JSONObject.fromObject(data);
        String modelName=String.valueOf(mapJson.get("modelName"));
        String giturl=String.valueOf(mapJson.get("gitBranch"));
        String starttime=String.valueOf(mapJson.get("startTime")).substring(0,10);
        String endtime=String.valueOf(mapJson.get("endTime")).substring(0,10);
        return predictService.train(giturl,starttime,endtime,modelName,getUserId.getUserId());
    }

    @RequestMapping("/delete")
    public JSONObject delete(@RequestBody String data){
        return predictService.delete(data,getUserId.getUserId());
    }

    @RequestMapping("/getTrainData")
    public JSONObject getTrainData(@RequestBody String data){return predictService.getTrainData(data);
    }

    @RequestMapping("/run")
    public JSONObject run(@RequestBody String data){
        return predictService.run(data,getUserId.getUserId());
    }
}