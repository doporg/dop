package com.clsaa.dop.server.image.feign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务接口
 */
@Component
@FeignClient(value = "user-server",configuration = FeignConfig.class)
public interface UserFegin {
    @GetMapping()
    public UserCredentialV1 getUserCredentialV1ByUserId(@PathVariable("userId") Long userId,
                                                        @RequestParam("type")UserCredentialType type);
}
