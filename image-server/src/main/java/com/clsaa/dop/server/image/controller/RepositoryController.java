package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.vo.RepositoryVO;
import com.clsaa.dop.server.image.service.RepositoryService;
import com.clsaa.dop.server.image.util.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用于管理镜像仓库的控制类
 * @author 2019-3-25
 */
@RestController
@CrossOrigin
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @ApiOperation(value = "获取某个项目的镜像仓库")
    @GetMapping(value = "/v1/projects/{projectId}/repositories")
    public List<RepositoryVO> getRepositories(@ApiParam(value = "项目id",required = true) @PathVariable(value = "projectId") Integer projectId,
                                              @ApiParam(value = "搜索条件") @RequestParam(value = "q", required = false) String q,
                                              @ApiParam(value = "排序方式") @RequestParam(value = "sort", required = false) String sort,
                                              @ApiParam(value = "标签id") @RequestParam(value = "labelId", required = false) Integer labelId,
                                              @ApiParam(value = "页号，默认为1") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(value = "页大小，默认为10，最大为100") @RequestParam(value = "page_size", required = false) Integer pageSize,
                                              @ApiParam(value = "用户id") @RequestHeader(value = "x-login-user")Long userId){

            return BeanUtils.convertList(repositoryService.getRepositories(projectId,q,sort,labelId,page,pageSize,userId),RepositoryVO.class);
    }

    @ApiOperation(value = "删除镜像仓库")
    @DeleteMapping(value = "/v1/projects/{projectId}/repositories/{repoName}")
    public void deleteRepository(@ApiParam(value = "项目id") @PathVariable(value = "projectId")Integer projectId,
                                 @ApiParam(value = "镜像仓库名称") @PathVariable(value = "repoName")String repoName,
                                 @ApiParam(value = "用户id") @RequestHeader(value = "x-login-user")Long userId){

    }




}
