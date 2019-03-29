package com.clsaa.dop.server.pipeline.feign;


import com.clsaa.dop.server.pipeline.config.FeignConfig;
import com.clsaa.dop.server.pipeline.config.HttpHeadersConfig;
import com.clsaa.dop.server.pipeline.model.dto.AppBasicInfoV1;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "application-server", configuration = FeignConfig.class)
public interface ApplicationFeign {

    /**
     * 根据id查询应用信息
     */
    @GetMapping(value = "/app/{appId}/urlInfo")
    AppBasicInfoV1 findAppById(@ApiParam(name = "appId", value = "appId", required = true) @PathVariable(value = "appId") Long appId);

    /**
     * 拿版本号
     * */
    @GetMapping(value = "/app/env/{appEnvId}/build_tag")
    String findBuildTagByAppEnvIdAndRunningId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "runningId", name = "运行ID", required = true) @RequestParam(value = "runningId") String runningId);

}
