package com.clsaa.dop.server.code.feign;

import com.clsaa.dop.server.code.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wsy
 */
@Component
@FeignClient(value = "user-server",configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping("/v1/account/RSAPublicKey")
    String getAccountRSAPublicKey();

}
