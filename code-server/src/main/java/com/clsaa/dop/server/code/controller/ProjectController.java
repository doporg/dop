package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.vo.ProjectVo;
import com.clsaa.dop.server.code.service.ProjectService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wsy
 */
@CrossOrigin
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "查询项目信息",notes="根据项目的id查询项目总览需要的信息")
    @GetMapping("/projects/{id}")
    public ProjectVo findProject(@ApiParam(value = "项目id") @PathVariable("id") int id){
        return BeanUtils.convertType(projectService.findProject(id),ProjectVo.class);
    }

}
