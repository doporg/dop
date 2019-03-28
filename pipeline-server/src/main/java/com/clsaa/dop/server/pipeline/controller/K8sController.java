package com.clsaa.dop.server.pipeline.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * k8s接口实现类
 *
 * @author 张富利
 * @since 2019-03-24
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class K8sController {

    @ApiOperation(value = "")
    @GetMapping("/v1/k8s/test")
    public void test() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://raw.githubusercontent.com/clsaa/dop/master/dop-web/k8s.yaml";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String result = responseEntity.getBody();
        if(responseEntity.getStatusCodeValue() == 200 && result!= null){
            String[] yamls = result.split("---");
            Yaml yaml = new Yaml();
            Map map = yaml.load(yamls[0]);
            System.out.println(map.get("kind"));
            Map test = (Map) map.get("metadata");
            System.out.println(test.get("namespace"));
        }
    }
}
