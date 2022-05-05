package cn.com.devopsplus.dop.server.defect.controller;

import cn.com.devopsplus.dop.server.defect.service.PredictService;
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
    public JSONObject get(){
        //用户ID
        Integer userId=1;

        return predictService.get(userId);
    }

    @ApiOperation(value = "添加模型", notes = "根据用户id添加模型信息")
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String data){
        //用户ID
        Integer userId=1;

        JSONObject mapJson=JSONObject.fromObject(data);
        String modelName=String.valueOf(mapJson.get("modelName"));
        String giturl=String.valueOf(mapJson.get("gitBranch"));
        String starttime=String.valueOf(mapJson.get("startTime")).substring(0,10);
        String endtime=String.valueOf(mapJson.get("endTime")).substring(0,10);
        return predictService.train(giturl,starttime,endtime,modelName,userId);
    }

    @RequestMapping("/delete")
    public JSONObject delete(@RequestBody String data){
        //用户ID
        Integer userId=1;
        return predictService.delete(data,userId);
    }

    @RequestMapping("/getTrainData")
    public JSONObject getTrainData(@RequestBody String data){return predictService.getTrainData(data);
    }

    @RequestMapping("/run")
    public JSONObject run(@RequestBody String data){
        //用户ID
        Integer userId=1;
        return predictService.run(data,userId);
    }
}
