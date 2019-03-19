package com.clsaa.dop.server.pipeline.controller;

import com.clsaa.dop.server.pipeline.config.BizCodes;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.service.BlueOceanService;
import com.clsaa.dop.server.pipeline.service.JenkinsService;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 流水线-jenkinsAPI接口实现类
 *
 * @author 张富利
 * @since 2019-03-15
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class JenkinsController {

    @Autowired
    private JenkinsService jenkinsService;
    @Autowired
    private RestTemplate restTemplate;

    private String BaseUrl = "http://jenkins.dop.clsaa.com";

    private BlueOceanService blueOceanService = new BlueOceanService();


    @ApiOperation(value = "获取Authorization", notes = "获取Authorization, 供前端访问blueocean接口")
    @GetMapping("/v1/authorization")
    public String authorization() {
        return this.blueOceanService.getAuthorization();
    }

    @ApiOperation(value = "删除流水线", notes = "根据id删除流水线")
    @DeleteMapping("/v1/jenkins/byId")
    public void delete(String id) {
        BizAssert.validParam(StringUtils.isNotBlank(id),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "参数id非法"));

        this.jenkinsService.deleteJob(id);
    }

    @ApiOperation(value = "创建流水线", notes = "根据流水线信息创建流水线")
    @PostMapping("/v1/jenkins")
    public void create(@RequestBody PipelineBoV1 pipelineBoV1) {
        this.jenkinsService.createJob(pipelineBoV1, "1.0");
    }

    @ApiOperation(value = "运行流水线", notes = "根据流水线id查找开始运行流水线")
    @PostMapping("/v1/jenkins/build")
    public String build(String id) {
        String url = this.BaseUrl + "/blue/rest/organizations/jenkins/pipelines/" + id +"/runs/";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,null, String.class);
        return responseEntity.getBody();
    }

    @ApiOperation(value = "查找流水线运行结果", notes = "根据流水线id查找流水线运行结果所得stages")
    @GetMapping("/v1/jenkins/runs")
    public String runs(String id) {
        String url = this.BaseUrl + "/blue/rest/organizations/jenkins/pipelines/" + id +"/runs/";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
        return responseEntity.getBody();
    }

    @ApiOperation(value = "查找流水线运行最后一次的stages", notes = "根据传进来的地址进行api接口的访问")
    @GetMapping("/v1/jenkins/result")
    public String stages(String path) {
        String url = this.BaseUrl + path;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
        return responseEntity.getBody();
    }
}
