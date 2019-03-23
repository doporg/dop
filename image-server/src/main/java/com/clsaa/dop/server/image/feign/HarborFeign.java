package com.clsaa.dop.server.image.feign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.po.ProjectPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://registry.dop.clsaa.com/api",name = "test", configuration = FeignConfig.class)
public interface HarborFeign {
    @GetMapping(value = "/projects/{project_id}")
    ProjectPO getProject(@RequestParam("project_id") Integer project_id);
}
