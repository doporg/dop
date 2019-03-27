package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.dto.ProjectDto1;
import com.clsaa.dop.server.image.model.dto.ProjectMetadataDto1;
import com.clsaa.dop.server.image.model.po.ProjectMetadata;
import com.clsaa.dop.server.image.model.vo.ProjectMetadataVO;
import com.clsaa.dop.server.image.model.vo.ProjectVO;
import com.clsaa.dop.server.image.service.ProjectService;
import com.clsaa.dop.server.image.util.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *     用户项目的控制器实现类
 * </p>
 * @author xzt
 * @since 2019-3-23
 */
@RestController
@Api(value = "ProjectController|一个用来处理Project请求的控制器")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "获取项目列表信息",notes = "根据权限的不同访问项目列表信息")
    @GetMapping("/v1/projects")
    public List<ProjectVO> getProjects(@ApiParam(value = "The name of project.")
                                           @Valid @RequestParam(value = "name", required = false) String name,
                                       @ApiParam(value = "The project is public or private.")
                                       @Valid @RequestParam(value = "public", required = false) Boolean _public,
                                       @ApiParam(value = "The name of project owner.")
                                           @Valid @RequestParam(value = "owner", required = false) String owner,
                                       @ApiParam(value = "The page nubmer, default is 1.")
                                           @Valid @RequestParam(value = "page", required = false) Integer page,
                                       @ApiParam(value = "The size of per page, default is 10, maximum is 100.")
                                           @Valid @RequestParam(value = "page_size", required = false) Integer pageSize){
        return BeanUtils.convertList(projectService.getProjects(name,_public,owner,page,pageSize),ProjectVO.class);
    }

    @ApiOperation(value = "获取某个项目的基本信息",notes = "需要给出项目的id进行查询，返回项目对象，不存在返回null")
    @GetMapping(value = "/v1/projects/{project_id}")
    public ProjectVO getProjectById(@ApiParam(value = "项目id",required = true) @PathVariable("project_id") Long project_id){
        return BeanUtils.convertType(projectService.getProjectById(project_id),ProjectVO.class);
    }

    @ApiOperation(value = "新建项目",notes = "需要给出项目的基本信息，返回http状态码")
    @PostMapping(value = "/v1/projects")
    public void addProject(@ApiParam(value = "项目名称",required = true) @RequestParam(value = "name") String projectName,
                           @ApiParam(value = "项目权限",required = true) @RequestParam(value = "status") String publicStatus
                          ){
        //TODO 将创建人联系起来
        ProjectMetadata metadata = new ProjectMetadata(publicStatus,"false","false","low","false");
        ProjectDto1 projectDto1 = new ProjectDto1(projectName,metadata);
        projectService.addProject(projectDto1);
    }

    @ApiOperation(value = "修改项目信息",notes = "根据项目id，来更改项目信息，返回http状态码")
    @PutMapping(value = "/v1/projects/{project_id}")
    public void putProject(@ApiParam(value = "项目 id",required = true) @PathVariable(value = "project_id")Long projectId,
                           @ApiParam(value = "项目内容",required = true) @RequestBody ProjectDto1 projectDto1){
        //TODO 根据不同的信息返回不同的状态码和信息
        projectService.putProject(projectId,projectDto1);
    }

    @ApiOperation(value = "项目删除" ,notes = "根据项目的id删除项目，返回http状态码")
    @DeleteMapping(value = "/v1/projects/{project_id}")
    public void deleteProject(@ApiParam(value = "项目id",required = true) @PathVariable("project_id")Long projectId){
        projectService.deleteProject(projectId);
    }

    @ApiOperation(value = "通过项目id获取项目的基本信息")
    @GetMapping(value = "/v1/projects/{project_id}/metadatas")
    public ProjectMetadataVO getMetadatas(@ApiParam(value = "项目id") @PathVariable(value = "project_id")Long projectId){
        return BeanUtils.convertType(projectService.getProjectMetadata(projectId),ProjectMetadataVO.class);
    }

    @ApiOperation(value = "修改项目的基本信息")
    @PostMapping(value = "/v1/projects/{project_id}/metadatas")
    public void updateProjectMetadata(@ApiParam(value = "项目id")@PathVariable(value = "project_id")Long projectId,
                                      @ApiParam(value = "项目的基本信息") @RequestBody ProjectMetadataDto1 projectMetadataDto1){
        //首先应该删除项目的信息
        deleteProjectMetadata(projectId);
        //添加新的项目信息进行更新
        projectService.addProjectMetadata(projectId,projectMetadataDto1);
    }

    @ApiOperation(value = "删除项目的基本信息")
    @DeleteMapping(value = "/v1/projects/{project_id}/metadatas")
    public void deleteProjectMetadata(@ApiParam(value = "项目id") @PathVariable(value = "project_id")Long projectId){
        //由于harbor没有提供删除，因此需要单个属性进行删除
        projectService.deleteProjectMetadata(projectId,"public");
        projectService.deleteProjectMetadata(projectId,"auto_scan");
        projectService.deleteProjectMetadata(projectId,"enable_content_trust");
        projectService.deleteProjectMetadata(projectId,"prevent_vul");
        projectService.deleteProjectMetadata(projectId,"severity");
    }


}
