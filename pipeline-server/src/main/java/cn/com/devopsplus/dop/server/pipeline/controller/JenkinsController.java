package cn.com.devopsplus.dop.server.pipeline.controller;

import cn.com.devopsplus.dop.server.pipeline.config.BizCodes;
import cn.com.devopsplus.dop.server.pipeline.config.HttpHeadersConfig;
import cn.com.devopsplus.dop.server.pipeline.service.BlueOceanService;
import cn.com.devopsplus.dop.server.pipeline.service.JenkinsService;
import cn.com.devopsplus.dop.server.pipeline.service.PipelineService;
import cn.com.devopsplus.dop.server.pipeline.service.ResultOutputService;
import cn.com.devopsplus.dop.server.pipeline.util.JenkinsUtils;
import cn.com.devopsplus.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private RestTemplate restTemplate;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ResultOutputService resultOutputService;
    @Autowired
    private JenkinsService jenkinsService;

    private JenkinsUtils jenkinsUtils = new JenkinsUtils();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    private String jenkinsUtils.getUri() = JenkinsUtils.getUri();

    private BlueOceanService blueOceanService = new BlueOceanService();


    @ApiOperation(value = "获取Authorization", notes = "获取Authorization, 供前端访问blueocean接口")
    @GetMapping("/v1/authorization")
    public String authorization() {
        logger.info("[authorization] Request coming: ");
        return this.blueOceanService.getAuthorization();
    }

    @ApiOperation(value = "删除流水线", notes = "根据id删除流水线")
    @DeleteMapping("/v1/jenkins/{id}")
    public void delete(@PathVariable(value = "id") String id) {
        BizAssert.validParam(StringUtils.isNotBlank(id),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "参数id非法"));
        logger.info("[delete] Request coming: id={}",id);
        this.jenkinsService.deleteJob(id);
    }

    @ApiOperation(value = "创建流水线", notes = "根据流水线信息创建流水线, 流水线的名称是id")
    @PostMapping("/v1/jenkins")
    public void create(@RequestBody Pipeline pipeline) {
        logger.info("[create] Request coming: pipeline");
        this.jenkinsService.createJob(pipeline);
    }

    @ApiOperation(value = "创建流水线", notes = "根据jenkinsfile创建流水线, 流水线的名称是id")
    @PostMapping("/v1/jenkins/jenkinsfile")
    public void jenkinsfile(@RequestBody Pipeline pipeline) {
        logger.info("[jenkinsfile] Request coming: pipeline");
        this.jenkinsService.createByJenkinsfile(pipeline);
    }

    @ApiOperation(value = "运行流水线", notes = "根据流水线id查找开始运行流水线")
    @PostMapping("/v1/jenkins/build/{id}")
    public void build(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @PathVariable(value = "id") String id
    ) {
        logger.info("[build] Request coming: loginUser={}, id={}",loginUser,id);
        //拿到 result output id
        String resultOutputId = this.resultOutputService.create(id);
        //拿到 校验流水线信息完整
        Pipeline pipeline = this.pipelineService.setInfo(id, resultOutputId, loginUser);

        //创建流水线
        if(pipeline.getConfig().equals(Pipeline.Config.HasJenkinsfile)){
            this.jenkinsService.createByJenkinsfile(pipeline);
        }else{
            this.jenkinsService.createJob(pipeline);
        }

        //运行流水线
        String url = this.jenkinsUtils.getUri() + "/blue/rest/organizations/jenkins/pipelines/" + id + "/runs/";
        logger.info("[build] Get the url: url={}",url);
        restTemplate.postForEntity(url, null, String.class);
    }

    @ApiOperation(value = "查找流水线运行结果", notes = "根据流水线id查找流水线运行结果所得stages")
    @GetMapping("/v1/jenkins/runs")
    public String runs(String id) {
        logger.info("[runs] Request coming: id={}",id);
        String url = this.jenkinsUtils.getUri() + "/blue/rest/organizations/jenkins/pipelines/" + id + "/runs/";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }

    @ApiOperation(value = "查找流水线运行最后一次的stages", notes = "根据传进来的地址进行api接口的访问")
    @GetMapping("/v1/jenkins/result")
    public String stages(String path) {
        logger.info("[stages] Request coming: path={}",path);
        String url = this.jenkinsUtils.getUri() + path;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }

    @ApiOperation(value = "查找流水线运行最后一次的状态", notes = "根据传进来的地址进行api接口的访问")
    @GetMapping("/v1/jenkins/result/status")
    public String status(String id) {
        logger.info("[status] Request coming: id={}",id);
        return this.jenkinsService.getBuildResult(id);
    }


}
