package cn.com.devopsplus.dop.server.defect.service;

import cn.com.devopsplus.dop.server.defect.util.Run;
import cn.com.devopsplus.dop.server.defect.config.PredictConfig;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public JSONObject prfl(String path,String locationMethod){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> mapdatalist=new ArrayList<>();
        String res=run.prfllocate(predictConfig.getPrflPythonPath(),path,locationMethod,predictConfig.getPythonProjectPath());

        if (res==null){
            map.put("success",false);
        }
        else {
            map.put("success",true);

            System.out.println(res);
            JSONArray entryArray = JSONArray.fromObject(res);
            List datalist=JSONArray.toList(entryArray, String.class);

            String str;
            int i=0;
            while (i<datalist.size()) {
                str= (String) datalist.get(i);
                Map<String,String> mapdata=new HashMap<>();
                System.out.println("i="+i);
                System.out.println("str="+str);
                mapdata.put("defectgrade",str.substring(0,str.indexOf("----")));
                mapdata.put("defectline",str.substring(str.indexOf("----")+4,str.length()));
                mapdata.put("id",String.valueOf(i+1));
                mapdatalist.add(mapdata);
                i++;
            }
            map.put("data",mapdatalist);
        }
        JSONObject JO = JSONObject.fromObject(map);
        return JO;
    }
}