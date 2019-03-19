package com.clsaa.dop.server.application.controller;


import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import com.clsaa.dop.server.application.model.po.AppVariable;
import com.clsaa.dop.server.application.model.vo.AppEnvDetailV1;
import com.clsaa.dop.server.application.model.vo.AppEnvV1;
import com.clsaa.dop.server.application.model.vo.ClusterInfoV1;
import com.clsaa.dop.server.application.service.AppEnvService;
import com.clsaa.dop.server.application.service.AppVarService;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.models.V1NamespaceList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.HashMap;
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

    @ApiOperation(value = "更新环境信息", notes = "根据应用环境信息ID更新应用环境")
    @PutMapping(value = "/application/environment/detail/yaml/{appEnvId}")
    public void updateOrCreateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", required = true) @RequestParam(value = "nameSpace") String nameSpace,
            @ApiParam(name = "service", value = "服务", required = true) @RequestParam(value = "service") String service,
            @ApiParam(name = "deployment", value = "部署", required = true) @RequestParam(value = "deployment") String deployment,
            @ApiParam(name = "containers", value = "容器", required = true) @RequestParam(value = "containers") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", required = true) @RequestParam(value = "replicas") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "image_url", value = "发布批次", defaultValue = "") @RequestParam(value = "image_url", defaultValue = "") String image_url) {
        this.appEnvService.updateOrCreateYamlInfoByAppEnvId(appEnvId, cuser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, image_url);
    }

    @ApiOperation(value = "获取命名空间", notes = "获取命名空间")
    @GetMapping(value = "/application/environment/detail/{id}/cluster/allNamespaces")
    public List<String> getNameSpaceByUrlAndToken(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id
    ) {
        try {
            return this.appEnvService.findNameSpaces(id);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取服务", notes = "获取服务")
    @GetMapping(value = "/application/environment/detail/{id}/cluster/allServices")
    public List<String> getServiceByNameSpace(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace
    ) {
        try {
            return this.appEnvService.getServiceByNameSpace(id, namespace);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取部署", notes = "获取部署")
    @GetMapping(value = "/application/environment/detail/{id}/cluster/allDeployment")
    public HashMap<String, Object> getDeploymentByNameSpaceAndService(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "service", name = "service", required = true) @RequestParam(value = "service") String service
    ) {
        try {
            return this.appEnvService.getDeploymentByNameSpaceAndService(id, namespace, service);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "创建服务", notes = "创建服务")
    @PostMapping(value = "/application/environment/detail/{id}/cluster/services")
    public void createServiceByNameSpace(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "name", name = "name", required = true) @RequestParam(value = "name") String name,
            @ApiParam(value = "port", name = "port", required = true) @RequestParam(value = "port") Integer port
    ) {
        try {
            this.appEnvService.createServiceByNameSpace(id, namespace, name, port);
        } catch (Exception e) {
            System.out.print(e);
        }
    }


    @ApiOperation(value = "更新集群信息", notes = "更新集群信息")
    @PostMapping(value = "/application/environment/detail/{id}/cluster")
    public void updateUrlAndToken(
            @ApiParam(value = "id", name = "id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "clusterInfoV1", name = "clusterInfoV1", required = true) @RequestBody ClusterInfoV1 clusterInfoV1) {
        try {
            this.appEnvService.updateUrlAndToken(id, clusterInfoV1.getTargetClusterUrl(), clusterInfoV1.getTargetClusterToken());
        } catch (Exception e) {
            System.out.print(e);
        }
    }


}
