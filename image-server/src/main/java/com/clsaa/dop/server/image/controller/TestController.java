package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.feign.HarborFeign;
import com.clsaa.dop.server.image.model.po.ProjectPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {
    @Autowired
    private HarborFeign harborFeign;
    @GetMapping(value = "/test/{project_id}")
    public ProjectPO getProjects(@PathVariable("project_id") Integer project_id){
       return harborFeign.getProject(project_id);
    }
}
