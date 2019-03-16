package com.clsaa.dop.server.application.controller;


import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import com.clsaa.dop.server.application.model.po.AppVariable;
import com.clsaa.dop.server.application.model.vo.AppEnvDetailV1;
import com.clsaa.dop.server.application.model.vo.AppEnvV1;
import com.clsaa.dop.server.application.service.AppEnvService;
import com.clsaa.dop.server.application.service.AppVarService;
import com.clsaa.dop.server.application.service.KubernetesService;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.models.V1NamespaceList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用环境API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-16
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class AppEnvController {
    @Autowired
    AppEnvService appEnvService;
    @Autowired
    KubernetesService kubernetesService;

    @ApiOperation(value = "查询应用环境信息", notes = "根据应用ID查询应用环境变量")
    @GetMapping(value = "/application/environment/{appId}")
    public List<AppEnvV1> findEnvironmentByAppId(
            @ApiParam(value = "appId", name = "appId", required = true) @PathVariable(value = "appId") Long appId) {
        return this.appEnvService.findEnvironmentByAppId(appId).stream().map(l -> BeanUtils.convertType(l, AppEnvV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "查询应用环境详情", notes = "根据应用环境信息ID查询应用环境详情")
    @GetMapping(value = "/application/environment/detail/{id}")
    public AppEnvDetailV1 findEnvironmentDetailById(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id) {
        return BeanUtils.convertType(this.appEnvService.findEnvironmentDetailById(id), AppEnvDetailV1.class);
    }


    @ApiOperation(value = "删除应用环境", notes = "根据应用环境ID删除应用环境")
    @DeleteMapping(value = "/application/environment/{id}")
    public void deleteEnvironmentById(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id) {
        this.appEnvService.deleteEnvironmentById(id);
    }


    @ApiOperation(value = "查询应用环境详情", notes = "根据应用环境信息ID查询应用环境详情")
    @PostMapping(value = "/application/environment/{appId}")
    public void createEnvironmentByAppId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appId", name = "appId", required = true) @PathVariable(value = "appId") Long appId,
            @ApiParam(name = "title", value = "环境名称", required = true) @RequestParam(value = "title") String title,
            @ApiParam(name = "environmentLevel", value = "环境级别", required = true) @RequestParam(value = "environmentLevel") String environmentLevel,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy) {
        this.appEnvService.createEnvironmentByAppId(appId, cuser, title, environmentLevel, deploymentStrategy);
    }

    @ApiOperation(value = "更新环境详情", notes = "根据应用环境信息ID更新应用环境详情")
    @PutMapping(value = "/application/environment/detail/{id}")
    public void updateEnvironmentDetailById(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long muser,
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(name = "targetCluster", value = "目标集群", required = true) @RequestParam(value = "targetCluster") String targetCluster,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", required = true) @RequestParam(value = "nameSpace") String nameSpace,
            @ApiParam(name = "service", value = "服务", required = true) @RequestParam(value = "service") String service,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch) {
        this.appEnvService.updateEnvironmentDetailById(id, muser, targetCluster, deploymentStrategy, nameSpace, service, releaseBatch);
    }

    @ApiOperation(value = "获取命名空间", notes = "获取命名空间")
    @GetMapping(value = "/application/environment/detail/cluster")
    public V1NamespaceList createEnvironmentDetailClusterById(
            @ApiParam(value = "集群ip", name = "url", required = true) @RequestParam(value = "url") String url,
            @ApiParam(value = "token", name = "token", required = true) @RequestParam(value = "token") String token
    ) {
        try {
            return this.kubernetesService.findNameSpaces(url, token);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }

    }


}
