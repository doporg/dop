package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.ProjectListBo;
import com.clsaa.dop.server.code.model.po.User;
import com.clsaa.dop.server.code.model.vo.ProjectListVo;
import com.clsaa.dop.server.code.model.vo.ProjectVo;
import com.clsaa.dop.server.code.service.ProjectService;
import com.clsaa.dop.server.code.service.UserService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@CrossOrigin
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询项目信息",notes="根据项目的id查询项目总览需要的信息")
    @GetMapping("/projects/{id}")
    public ProjectVo findProject(@ApiParam(value = "项目id") @PathVariable("id") int id){
        return BeanUtils.convertType(projectService.findProject(id),ProjectVo.class);
    }

    @GetMapping("/users/{username}/projects")
    public List<ProjectListVo> findProjectByMember(@ApiParam(value = "用户名")@PathVariable("username")String username){
        List<ProjectListBo> listBos= projectService.findProjectByMember(username);
        List<ProjectListVo> listVos=new ArrayList<>();
        for(ProjectListBo temp:listBos)
            listVos.add(BeanUtils.convertType(temp,ProjectListVo.class));
        return listVos;
    }



}
