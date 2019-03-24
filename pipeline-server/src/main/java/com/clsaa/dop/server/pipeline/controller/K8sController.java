package com.clsaa.dop.server.pipeline.controller;


import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1beta2Deployment;
import io.kubernetes.client.util.Config;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * k8s接口实现类
 *
 * @author 张富利
 * @since 2019-03-24
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class K8sController {

    @ApiOperation(value = "获取Authorization", notes = "获取Authorization, 供前端访问blueocean接口")
    @GetMapping("/v1/k8s/test")
    public void test() {
//        return this.blueOceanService.getAuthorization();
        try{
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);




        }catch (Exception e){

        }
    }
}
