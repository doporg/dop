package cn.com.devopsplus.dop.server.defect.service;

import cn.com.devopsplus.dop.server.defect.util.Run;
import cn.com.devopsplus.dop.server.defect.config.PredictConfig;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationService {
    @Autowired
    PredictConfig predictConfig;
    @Autowired
    Run run;
    public JSONObject sbfl(String path){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();
        String re=run.sbfllocate(predictConfig.getPythonInterpreterPath(),path,predictConfig.getSbflPythonPath()
                ,predictConfig.getPythonProjectPath());

        if (re==null||!re.equals("locate success")){
            map.put("success",false);
        }
        else {
            String resultfile=predictConfig.getSbflresultPath();
            try {
                FileReader fr = new FileReader(resultfile);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                int id=1;
                while ((str = bf.readLine()) != null) {
                    Map<String,String> mapdata=new HashMap<>();
                    mapdata.put("defectgrade",str.substring(0,str.indexOf("----")));
                    mapdata.put("defectline",str.substring(str.indexOf("----")+4,str.length()));
                    mapdata.put("id",String.valueOf(id));
                    mapdatalist.add(mapdata);
                    id++;
                }
                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("success",true);
            map.put("data",mapdatalist);
        }
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
}
