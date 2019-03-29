package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.project.ProjectListBo;
import com.clsaa.dop.server.code.model.dto.project.ProjectDto;
import com.clsaa.dop.server.code.model.vo.project.BranchVo;
import com.clsaa.dop.server.code.model.vo.project.ProjectEditVo;
import com.clsaa.dop.server.code.model.vo.project.ProjectListVo;
import com.clsaa.dop.server.code.model.vo.project.ProjectVo;
import com.clsaa.dop.server.code.service.ProjectService;
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
    @PostMapping("/projects/{id}/star")
    public void starProject(@ApiParam(value = "项目id") @PathVariable("id")int id,
                            @ApiParam(value = "用户名") @RequestParam("username")String username){
        projectService.starProject(id,username);
    }

    @ApiOperation(value = "查找用户参与的项目",notes = "根据用户名查找用户参与的项目")
    @GetMapping("/projectlist")
    public List<ProjectListVo> findProjectList(@ApiParam(value = "分类")@RequestParam("sort")String sort,
                                               @ApiParam(value = "用户名")@RequestParam("username")String username){
        List<ProjectListBo> listBos= projectService.findProjectList(sort,username);
        List<ProjectListVo> listVos=new ArrayList<>();
        for(ProjectListBo temp:listBos)
            listVos.add(BeanUtils.convertType(temp,ProjectListVo.class));
        return listVos;
    }

    @ApiOperation(value="新建一个项目",notes = "新建一个项目，包括一些基本信息")
    @PostMapping("/projects")
    public void addProject(@ApiParam(value = "项目基本信息")@RequestBody ProjectDto projectDto){
        projectService.addProject(
                projectDto.getName(),
                projectDto.getDescription(),
                projectDto.getVisibility(),
                projectDto.getInitialize_with_readme(),
                projectDto.getUsername()
        );
    }

    @ApiOperation(value = "查询编辑项目需要的信息",notes = "根据项目id查询")
    @GetMapping("/projects/{id}/editinfo")
    public ProjectEditVo findProjectEditInfo(@ApiParam(value = "项目id")@PathVariable("id") int id,
                                             @ApiParam(value = "用户名")@RequestParam("username") String username){
        return BeanUtils.convertType(projectService.findProjectEditInfo(id,username),ProjectEditVo.class);
    }

    @ApiOperation(value = "获得项目所有的分支名",notes = "根据项目id查询")
    @GetMapping("/projects/{id}/branches")
    public List<BranchVo> findAllBranchName(@ApiParam(value = "项目id")@PathVariable("id") int id,
                                          @ApiParam(value = "用户名")@RequestParam("username") String username){

        List<String> strs=projectService.findAllBranchName(id,username);

        List<BranchVo> branchVos=new ArrayList<>();
        for(String str:strs){
            branchVos.add(new BranchVo(str,str));
        }

        return branchVos;
    }

    @ApiOperation(value = "编辑项目信息",notes = "编辑项目信息包括名称、描述、默认分支和可见等级")
    @PutMapping("/projects/{id}")
    public void editProjectInfo(@ApiParam(value = "项目id")@PathVariable("id") int id,
                                @ApiParam(value = "项目名称")@RequestParam("name") String name,
                                @ApiParam(value = "项目描述")@RequestParam("description") String description,
                                @ApiParam(value = "默认分支")@RequestParam("default_branch") String default_branch,
                                @ApiParam(value = "可见等级")@RequestParam("visibility") String visibility,
                                @ApiParam(value = "用户名")@RequestParam("username") String username){

        projectService.editProjectInfo(id,name,description,default_branch,visibility,username);
    }

    @ApiOperation(value = "删除一个项目",notes = "根据项目id删除")
    @DeleteMapping("/projects/{id}")
    public void deleteProject(@ApiParam(value = "项目id")@PathVariable("id") int id,
                              @ApiParam(value = "用户名")@RequestParam("username") String username){
        projectService.deleteProject(id,username);
    }



}
