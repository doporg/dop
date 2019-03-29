package com.clsaa.dop.server.image.feign.harborfeign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "user",url = "${feign.url}",configuration = FeignConfig.class)
public interface HarborUserFeign {

    /**
     * 注册harbor用户
     * @param user 用户信息
     */
    @PostMapping(value = "/users")
    void usersPost(@RequestBody User user);

    


}
