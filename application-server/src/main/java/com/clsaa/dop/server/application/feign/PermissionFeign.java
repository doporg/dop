package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "permission-server", configuration = FeignConfig.class)
public interface PermissionFeign {

    /**
     * 查询用户是否拥有某权限
     *
     * @return boolean
     */
    @GetMapping("/v1/users/permissionmaps")
    boolean permissionCheck(@RequestParam(value = "permissionName") String permissionName,
                            @RequestParam("userId") Long loginUser);
}
