package cn.com.devopsplus.dop.server.defect.controller;

import cn.com.devopsplus.dop.server.defect.service.PredictService;
import cn.com.devopsplus.dop.server.defect.config.HttpHeaders;
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

    @RequestMapping("/get")
    public JSONObject get(@RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserID){
        return predictService.get(loginUserID.intValue());
    }

    @ApiOperation(value = "添加模型", notes = "根据用户id添加模型信息")
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String data,@RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserID){
        JSONObject mapJson=JSONObject.fromObject(data);
        String modelName=String.valueOf(mapJson.get("modelName"));
        String giturl=String.valueOf(mapJson.get("gitBranch"));
        String starttime=String.valueOf(mapJson.get("startTime")).substring(0,10);
        String endtime=String.valueOf(mapJson.get("endTime")).substring(0,10);
        return predictService.train(giturl,starttime,endtime,modelName,loginUserID.intValue());
    }

    @RequestMapping("/delete")
    public JSONObject delete(@RequestBody String data,@RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserID){
        return predictService.delete(data,loginUserID.intValue());
    }

    @RequestMapping("/getTrainData")
    public JSONObject getTrainData(@RequestBody String data){return predictService.getTrainData(data);
    }

    @RequestMapping("/run")
    public JSONObject run(@RequestBody String data,@RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserID){
        return predictService.run(data,loginUserID.intValue());
    }
}