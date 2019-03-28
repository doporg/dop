package com.clsaa.dop.server.pipeline.feign;


import com.clsaa.dop.server.pipeline.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "application-server", configuration = FeignConfig.class)
public interface ApplicationFeign {

    /**
     * 根据id查询应用信息
     *
     */
//    @GetMapping(value="/app/{appId}/urlInfo")
}
