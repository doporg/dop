package cn.com.devopsplus.dop.server.pipeline.controller;

import cn.com.devopsplus.dop.server.pipeline.config.HttpHeadersConfig;
import cn.com.devopsplus.dop.server.pipeline.service.JenkinsService;
import cn.com.devopsplus.dop.server.pipeline.util.BeanUtils;
import cn.com.devopsplus.dop.server.pipeline.model.vo.ResultOutputV1;
import cn.com.devopsplus.dop.server.pipeline.service.ResultOutputService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "新建一个用户的流水线运行结果")
    @PostMapping("/v1/resultOutput")
    public void create(String id) {
        logger.info("[create] Request coming: id={}",id);
        this.resultOutputService.create(id);
    }

    @Async
    @ApiOperation(value = "通知变更result")
    @PostMapping("/v1/resultOutput/notify/{id}")
    public void notify(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @PathVariable(value = "id") String id) {
        logger.info("[notify] Request coming: loginUser={}, id={}",loginUser,id);
        String output = this.jenkinsService.getBuildOutputText(id);
        this.resultOutputService.setResult(id, output, loginUser);
    }

    @ApiOperation(value = "根据runningId拿日志")
    @GetMapping("/v1/resultOutput")
    public ResultOutputV1 findByRunningId(@RequestParam(value = "runningId") String runningId) {
        logger.info("[findByRunningId] Request coming: runningId={}",runningId);
        return BeanUtils.convertType(this.resultOutputService.findByRunningId(runningId), ResultOutputV1.class);
    }
}

