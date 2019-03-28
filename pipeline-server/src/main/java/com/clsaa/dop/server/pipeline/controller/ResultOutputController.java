package com.clsaa.dop.server.pipeline.controller;

import com.clsaa.dop.server.pipeline.model.po.Pipeline;
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
    public void create(Pipeline pipeline) {
        this.resultOutputService.create(pipeline);
    }

    @Async
    @ApiOperation(value = "通知变更result")
    @PostMapping("/v1/resultOutput/notify/{id}")
    public void notify(@PathVariable(value = "id") String id) {
        String output = this.jenkinsService.getBuildOutputText(id);
        this.resultOutputService.setResult(id, output);
    }

    @ApiOperation(value = "设置一个result")
    @PostMapping("/v1/resultOutput/result")
    public void setResult(String id, String output) {
        this.resultOutputService.setResult(id, output);
    }

    @ApiOperation(value = "删除一条用户记录")
    @PutMapping("/v1/resultOutput/{id}")
    public void delete(@PathVariable(value = "id") String id) {
        this.resultOutputService.delete(id);
    }
}

