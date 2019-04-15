package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import com.clsaa.dop.server.application.model.vo.PipelineIdAndNameV1;
import com.clsaa.dop.server.application.model.vo.UserNameV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "pipeline-server", configuration = FeignConfig.class)
public interface PipelineFeign {
    /**
     * 通过Id获取用户名称
     *
     * @return {@link UserNameV1}
     */
    @GetMapping("/v1/pipeline/envId/{envId}")
    PipelineIdAndNameV1 findPipelineByAppEnvId(@PathVariable("envId") Long envId);
}
