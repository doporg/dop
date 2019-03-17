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
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "查询项目信息",notes="根据项目的id查询项目总览需要的信息")
    @GetMapping("/projects/{id}")
    public ProjectVo findProject(@ApiParam(value = "项目id") @PathVariable("id") int id){
        return BeanUtils.convertType(projectService.findProject(id),ProjectVo.class);
    }

    @ApiOperation(value = "star一个项目",notes = "若项目没有star则star,否则unstar")
    @PostMapping("/projects/{id}/star/{username}")
    public void starProject(@ApiParam(value = "项目id") @PathVariable("id")int id,@ApiParam(value = "用户名") @PathVariable("username")String username){
        projectService.starProject(id,username);
    }

    @ApiOperation(value = "查找用户参与的项目",notes = "根据用户名查找用户参与的项目")
    @GetMapping("/projectlist/{sort}/{username}")
    public List<ProjectListVo> findProjectList(@ApiParam(value = "分类")@PathVariable("sort")String sort,@ApiParam(value = "用户名")@PathVariable("username")String username){
        List<ProjectListBo> listBos= projectService.findProjectList(sort,username);
        List<ProjectListVo> listVos=new ArrayList<>();
        for(ProjectListBo temp:listBos)
            listVos.add(BeanUtils.convertType(temp,ProjectListVo.class));
        return listVos;
    }



}
