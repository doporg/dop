package com.clsaa.dop.server.pipeline.controller;

import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import com.clsaa.dop.server.pipeline.service.JenkinsService;
import com.clsaa.dop.server.pipeline.service.ResultOutputService;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.util.Config;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;


/**
 * 流水线运行结果接口实现类
 *
 * @author 张富利
 * @since 2019-03-26
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResultOutputController {
    @Autowired
    ResultOutputService resultOutputService;
    @Autowired
    JenkinsService jenkinsService;

    @ApiOperation(value = "新建一个用户的流水线运行结果")
    @PostMapping("/v1/resultOutput")
    public void create(String id) {
        this.resultOutputService.create(id);
    }

    @Async
    @ApiOperation(value = "通知变更result")
    @PostMapping("/v1/resultOutput/notify/{id}")
    public void notify(@PathVariable(value = "id") String id) {
        String output = this.jenkinsService.getBuildOutputText(id);
        this.resultOutputService.setResult(id, output);
    }

    @ApiOperation(value = "根据runningId拿日志")
    @GetMapping("/v1/resultOutput/{runningId}")
    public ResultOutput findByRunningId(@PathVariable(value = "runningId") String runningId) {
        return this.resultOutputService.findByRunningId(runningId);
    }
}

