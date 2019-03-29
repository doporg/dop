package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.bo.AppBoV1;
import com.clsaa.dop.server.application.model.bo.AppUrlInfoBoV1;
import com.clsaa.dop.server.application.model.vo.AppBasicInfoV1;
import com.clsaa.dop.server.application.model.vo.AppV1;
import com.clsaa.dop.server.application.service.AppService;
import com.clsaa.dop.server.application.service.AppUrlInfoService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

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
public class AppController {
    @Autowired
    private AppService appService;
    @Autowired
    private AppUrlInfoService appUrlInfoService;


    @ApiOperation(value = "查询应用", notes = "根据项目ID查询应用项目")
    @GetMapping(value = "/pagedapp")
    public Pagination<AppV1> findApplicationByProjectId(@ApiParam(name = "pageNo", value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @ApiParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                        @ApiParam(name = "projectId", value = "项目ID", required = true) @RequestParam(value = "projectId") Long projectId,
                                                        @ApiParam(name = "queryKey", value = "搜索关键字", defaultValue = "") @RequestParam(value = "queryKey", defaultValue = "") String queryKey) {

        return this.appService.findApplicationByProjectIdOrderByCtimeWithPage(pageNo, pageSize, projectId, queryKey);
    }

    @ApiOperation(value = "查询应用", notes = "根据拥有者ID查询应用项目")
    @GetMapping(value = "/app?ouser=:ouser")
    public List<AppV1> findApplicationByCuser(
            @ApiParam(name = "ouser", value = "ouser", required = true) @RequestParam(value = "projectId") Long ouser) {

        return this.appService.findApplicationByOuser(ouser).stream().map(l->BeanUtils.convertType(l,AppV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "根据ID查询应用信息", notes = "根据ID查询应用项目")
    @GetMapping(value = "/app/{appId}/urlInfo")
    public AppBasicInfoV1 findAppById(@ApiParam(name = "appId", value = "appId", required = true) @PathVariable(value = "appId") Long appId) {
        System.out.print(appId);
        AppUrlInfoBoV1 appUrlInfoBoV1 = this.appUrlInfoService.findAppUrlInfoByAppId(appId);
        AppBoV1 app = this.appService.findAppById(appId);
        System.out.print(app);
        System.out.print(app.getCtime());
        AppBasicInfoV1 appBasicInfoV1 = AppBasicInfoV1.builder()
                .ctime(app.getCtime())
                .title(app.getTitle())
                .description(app.getDescription())
                .ouser(app.getOuser())
                .warehouseUrl(appUrlInfoBoV1.getWarehouseUrl())
                .productionDbUrl(appUrlInfoBoV1.getProductionDbUrl())
                .testDbUrl(appUrlInfoBoV1.getTestDbUrl())
                .productionDomain(appUrlInfoBoV1.getProductionDomain())
                .testDomain(appUrlInfoBoV1.getTestDomain())
                .imageUrl(appUrlInfoBoV1.getImageUrl())
                .build();

        return appBasicInfoV1;
    }


    @ApiOperation(value = "创建应用", notes = "创建应用")
    @PostMapping(value = "/app/{projectId}")
    @ResponseBody
    public void createApp(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
                          @ApiParam(name = "projectId", value = "项目Id", required = true) @PathVariable(value = "projectId") Long projectId,
                          @ApiParam(name = "title", value = "应用名称", required = true) @RequestParam(value = "title") String title,
                          @ApiParam(name = "description", value = "应用描述", defaultValue = "") @RequestParam(value = "description", required = false) String description,
                          @ApiParam(name = "productMode", value = "开发模式", required = true) @RequestParam(value = "productMode") String productMode,
                          @ApiParam(name = "gitUrl", value = "Git仓库地址", defaultValue = "") @RequestParam(value = "gitUrl", required = false) String gitUrl,
                          @ApiParam(name = "imageUrl", value = "镜像仓库地址", defaultValue = "") @RequestParam(value = "imageUrl", required = false) String imageUrl) {
        this.appService.createApp(cuser, projectId, title, description, productMode, gitUrl, imageUrl);
        return;
    }


    @ApiOperation(value = "更新应用", notes = "更新应用")
    @PutMapping(value = "/app/{appId}")
    public void updateApp(@ApiParam(name = "appId", value = "应用Id", required = true) @PathVariable(value = "appId") Long appId,
                          @ApiParam(name = "description", value = "应用描述", required = true) @RequestParam(value = "description") String description) {
        this.appService.updateApp(appId, description);
    }

    @ApiOperation(value = "删除应用", notes = "删除应用")
    @DeleteMapping(value = "/app/{appId}")
    public void deleteApp(
            @ApiParam(name = "appId", value = "应用Id", required = true) @PathVariable(value = "appId") Long appId) {
        this.appService.deleteApp(appId);

    }
}