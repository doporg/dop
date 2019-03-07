package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.model.bo.AppBoV1;
import com.clsaa.dop.server.application.model.vo.AppV1;
import com.clsaa.dop.server.application.service.ApplicationService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-7
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;


    @ApiOperation(value = "查询应用", notes = "根据项目ID查询应用项目")
    @GetMapping(value = "/applications")
    public Pagination<AppV1> findApplicationByProjectId(@ApiParam(name = "pageNo", value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @ApiParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                        @ApiParam(name = "projectId", value = "项目ID", required = true) @RequestParam(value = "projectId") Long projectId,
                                                        @ApiParam(name = "queryKey", value = "搜索关键字", defaultValue = "") @RequestParam(value = "queryKey", defaultValue = "") String queryKey) {


        Pagination<AppBoV1> paginationBoV1 = this.applicationService.findApplicationByProjectIdOrderByCtimeWithPage(pageNo, pageSize, projectId, queryKey);
        List<AppBoV1> AppBoV1List = paginationBoV1.getPageList();
        Integer totalCount = paginationBoV1.getTotalCount();

        //新建VO层对象 并赋值
        Pagination<AppV1> paginationV1 = new Pagination<>();
        paginationV1.setPageNo(pageNo);
        paginationV1.setPageSize(pageSize);
        paginationV1.setTotalCount(totalCount);
        if (totalCount == 0) {
            paginationV1.setPageList(Collections.emptyList());
            return paginationV1;
        }
        paginationV1.setPageList(AppBoV1List.stream().map(l -> BeanUtils.convertType(l, AppV1.class)).collect(Collectors.toList()));

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

    @ApiOperation(value = "创建应用", notes = "创建应用")
    @PostMapping(value = "/applications")
    public void createApp(
            @ApiParam(name = "projectId", value = "项目Id", required = true) @RequestParam(value = "projectId") Long projectId,
            @ApiParam(name = "title", value = "应用名称", required = true) @RequestParam(value = "title") String title,
            @ApiParam(name = "projectDescription", value = "应用描述", defaultValue = "") @RequestParam(value = "appDescription", required = false) String appDescription) {


        this.applicationService.createApp(projectId, title, appDescription);
        return;
    }


}
