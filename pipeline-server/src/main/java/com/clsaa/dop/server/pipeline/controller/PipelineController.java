package com.clsaa.dop.server.pipeline.controller;

import com.clsaa.dop.server.pipeline.model.vo.PipelineV1;
import com.clsaa.dop.server.pipeline.service.PipelineService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 流水线API接口实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */

@CrossOrigin
@RestController
public class PipelineController {
    @Autowired
    private PipelineService pipelineService;

    @ApiOperation(value = "添加流水线", notes = "流水线信息: 一条流水线可以有多个阶段(stage), 一个阶段可以执行多条任务(task)")
    @PostMapping("/v1/pipeline")
    public void addUserV1(@RequestBody PipelineV1 pipelineV1) {
        this.pipelineService.addPipeline(pipelineV1);
    }

}
