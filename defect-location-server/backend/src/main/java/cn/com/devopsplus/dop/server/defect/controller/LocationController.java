package cn.com.devopsplus.dop.server.defect.controller;

import cn.com.devopsplus.dop.server.defect.config.PredictConfig;
import cn.com.devopsplus.dop.server.defect.service.LocationService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/defect")
public class LocationController {
    @Autowired
    LocationService locationService;
    @Autowired
    PredictConfig predictConfig;
    @RequestMapping("/locate")
    public JSONObject sbfl(@RequestBody String data){
        JSONObject mapJson=JSONObject.fromObject(data);
        System.out.println(data);
        String content=String.valueOf(mapJson.get("file"));
        String path=predictConfig.getPythonProjectPath()+"/defect_features/SBFL/test.txt";
        try {
            FileWriter fr = new FileWriter(path);
            BufferedWriter bf = new BufferedWriter(fr);
            bf.write(content);
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationService.sbfl(path);
    }
}