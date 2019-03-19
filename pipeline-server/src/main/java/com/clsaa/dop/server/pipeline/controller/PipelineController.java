package com.clsaa.dop.server.pipeline.controller;

import com.clsaa.dop.server.pipeline.config.BizCodes;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV1;
import com.clsaa.dop.server.pipeline.service.PipelineService;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


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



    @ApiOperation(value = "添加流水线", notes = "流水线信息: 一条流水线可以有多个阶段(stage), 一个阶段可以执行多条任务(task)，成功返回status===200，失败返回400")
    @PostMapping("/v1/pipeline")
    public void addUserV1(@RequestBody PipelineVoV1 pipelineV1) {
        try{
            this.pipelineService.addPipeline(pipelineV1);
        }catch (Exception e){
            BizAssert.justInvalidParam(BizCodes.ERROR_INSERT);
        }
    }

    @ApiOperation(value = "查找所有流水线信息", notes = "查找所有流水线信息，若成功返回流水线所有的信息，失败返回null")
    @GetMapping("/v1/pipelines")
    public List<PipelineBoV1> findAll() {
            return this.pipelineService.findAll();
    }

    @ApiOperation(value = "根据id删除流水线", notes = "根据id对流水线进行逻辑删除")
    @PutMapping("/v1/delete/byId")
    public void deleteById(String id) {
        BizAssert.validParam(StringUtils.isNotBlank(id),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "参数id非法"));
        this.pipelineService.deleteById(id);
    }

    @ApiOperation(value = "根据id查找流水线信息")
    @GetMapping("/v1/pipeline/byId")
    public PipelineBoV1 findById(String id) {
        BizAssert.validParam(StringUtils.isNotBlank(id),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "参数id非法"));
        return this.pipelineService.findById(new ObjectId(id));
    }

}
