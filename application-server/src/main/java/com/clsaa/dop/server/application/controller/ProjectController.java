package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.model.bo.ProjectBoV1;
import com.clsaa.dop.server.application.service.ProjectService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import com.clsaa.dop.server.application.model.vo.ProjectV1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 项目API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-5
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class ProjectController {
    @Autowired
    private ProjectService projectService;


    @ApiOperation(value = "分页查询项目信息", notes = "分页查询项目信息, never return null")
    @GetMapping(value = "/project")
    public Pagination<ProjectV1> findProjectOrderByCtimeWithPage(@ApiParam(name = "pageNo", value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @ApiParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 @ApiParam(name = "includeFinished", value = "是否包含已结项目", required = true, defaultValue = "false") @RequestParam(value = "includeFinished", defaultValue = "false") Boolean includeFinished,
                                                                 @ApiParam(name = "queryKey", value = "搜索关键字", defaultValue = "") @RequestParam(value = "queryKey", defaultValue = "") String queryKey) {

        Pagination<ProjectBoV1> paginationBoV1 = this.projectService.findProjectOrderByCtimeWithPage(pageNo, pageSize, includeFinished, queryKey);
        List<ProjectBoV1> projectBoV1List = paginationBoV1.getPageList();
        Integer totalCount = paginationBoV1.getTotalCount();


        //新建VO层对象并赋值 返回
        Pagination<ProjectV1> paginationV1 = new Pagination<>();
        paginationV1.setPageNo(pageNo);
        paginationV1.setPageSize(pageSize);
        paginationV1.setTotalCount(totalCount);
        if (totalCount == 0) {
            paginationV1.setPageList(Collections.emptyList());
            return paginationV1;
        }
        paginationV1.setPageList(projectBoV1List.stream().map(l -> BeanUtils.convertType(l, ProjectV1.class)).collect(Collectors.toList()));

        return paginationV1;
    }

    public interface HttpHeaders {
        /**
         * 用户登录Token请求头
         */
        String X_LOGIN_TOKEN = "x-login-token";
        /**
         * 登录用户id
         */
        String X_LOGIN_USER = "x-login-user";
    }
    @ApiOperation(value = "创建项目", notes = "创建项目")
    @PostMapping(value = "/project")

    public void createProject(@RequestHeader(HttpHeaders.X_LOGIN_USER) Long cuser,
                              @ApiParam(name = "title", value = "项目名称", required = true) @RequestParam(value = "title") String title,
                              @ApiParam(name = "projectDescription", value = "项目描述", defaultValue = "") @RequestParam(value = "projectDescription", required = false) String projectDescription) {


        this.projectService.createProjects(cuser, title, projectDescription);
        return;
    }


}