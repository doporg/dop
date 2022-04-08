package cn.com.devopsplus.dop.server.pipeline.controller;

import cn.com.devopsplus.dop.server.pipeline.config.HttpHeadersConfig;
import cn.com.devopsplus.dop.server.pipeline.dao.PipelineRepository;
import cn.com.devopsplus.dop.server.pipeline.service.PipelineService;
import cn.com.devopsplus.dop.server.pipeline.model.bo.PipelineV1Project;
import cn.com.devopsplus.dop.server.pipeline.model.po.Pipeline;
import cn.com.devopsplus.dop.server.pipeline.model.vo.PipelineVoV1;
import cn.com.devopsplus.dop.server.pipeline.model.vo.PipelineVoV3;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流水线API接口实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PipelineController {
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelineRepository pipelineRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "添加流水线", notes = "流水线信息: 一条流水线可以有多个阶段(stage), 一个阶段可以执行多条任务(step)，成功返回status===200，失败返回400")
    @PostMapping("/v1/pipeline")
    public String addPipeline(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @RequestBody Pipeline pipeline
    ){
        logger.info("[addUserV1] Request coming: loginUser={}, pipeline",loginUser);
        return this.pipelineService.addPipeline(pipeline, loginUser);
    }

    @ApiOperation(value = "通过jenkinsfile添加流水线", notes = "流水线信息: 一条流水线可以有多个阶段(stage), 一个阶段可以执行多条任务(step)，成功返回status===200，失败返回400")
    @PostMapping("/v1/pipeline/jenkinsfile")
    public void addUserV1Byjenkinsfile(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @RequestBody Pipeline pipeline) {
        logger.info("[addUserV1Byjenkinsfile] Request coming: loginUser={}, pipeline",loginUser);
        this.pipelineService.addPipelineWithJenkins(pipeline, loginUser);
    }

    @ApiOperation(value = "查找简略流水线信息, 集中展示")
    @GetMapping("/v1/pipelines/brief")
    public List<PipelineVoV3> getPipelineForTable() {
        logger.info("[getPipelineForTable] Request coming: ");
        return this.pipelineService.getPipelineForTable();
    }

    @ApiOperation(value = "根据id删除流水线", notes = "根据id对流水线进行逻辑删除")
    @PutMapping("/v1/delete/{id}")
    public Pipeline deleteById(@PathVariable(value = "id") String id) {
        logger.info("[deleteById] Request coming: id={}",id);
        return this.pipelineService.deleteById(id);
    }


    @ApiOperation(value = "查找所有流水线信息", notes = "查找所有流水线信息，若成功返回流水线所有的信息(未被删除)，失败返回null")
    @GetMapping("/v1/pipelines")
    public List<Pipeline> findAll() {
        logger.info("[findAll] Request coming: ");
        return this.pipelineService.findAll();
    }


    @ApiOperation(value = "根据id查找流水线信息")
    @GetMapping("/v1/pipeline/{id}")
    public PipelineVoV1 findByIdV1(@PathVariable(value = "id") String id) {
        logger.info("[findByIdV1] Request coming: id={}",id);
        return this.pipelineService.findByIdV1(id);
    }

    @ApiOperation(value = "根据用户id查找，返回该用户的流水线信息")
    @GetMapping("/v1/pipeline/cuser")
    public List<PipelineV1Project> getPipelineById(Long cuser) {
        logger.info("[getPipelineById] Request coming: cuser={}",cuser);
        return this.pipelineService.getPipelineById(cuser);
    }

    @ApiOperation(value = "根据流水线信息id, 设置appid和envid")
    @PostMapping("/v1/pipeline/appId")
    public void setAppId(String pipelineId, Long appid, Long envid) {
        logger.info("[setAppId] Request coming: pipelineId={}, appid={}, envid={}",pipelineId,appid,envid);
        List<Pipeline> pipelines = this.pipelineRepository.findByAppEnvId(envid);
        if (pipelines.size() != 0) {
            for (int i = 0; i < pipelines.size(); i++) {
                Pipeline pipeline = pipelines.get(i);
                pipeline.setAppEnvId(null);
                this.pipelineRepository.save(pipeline);
            }
        }
        Pipeline pipelineSetInfo = this.pipelineService.findById(pipelineId);
        pipelineSetInfo.setAppId(appid);
        pipelineSetInfo.setAppEnvId(envid);
        this.pipelineService.update(pipelineSetInfo);
    }

    @ApiOperation(value = "根据流水线env-id, 查询pipelineid")
    @GetMapping("/v1/pipeline/envId/{envId}")
    public List<PipelineV1Project> getPipelineIdByEnvId(@PathVariable(value = "envId") Long envId) {
        logger.info("[getPipelineIdByEnvId] Request coming: envId={}",envId);
        return this.pipelineService.getPipelineIdByEnvId(envId);
    }

    @ApiOperation(value = "根据id更新流水线信息")
    @PutMapping("/v1/pipeline")
    public void update(@RequestBody Pipeline pipeline) {
        logger.info("[update] Request coming: pipeline");
        this.pipelineService.update(pipeline);
    }


}
