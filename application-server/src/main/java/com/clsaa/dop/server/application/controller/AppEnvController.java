package com.clsaa.dop.server.application.controller;


import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.bo.AppEnvBoV1;
import com.clsaa.dop.server.application.model.bo.KubeCredentialBoV1;
import com.clsaa.dop.server.application.model.bo.KubeYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.KubeCredential;
import com.clsaa.dop.server.application.model.vo.AppEnvK8sV1;
import com.clsaa.dop.server.application.model.vo.AppEnvPipelineV1;
import com.clsaa.dop.server.application.model.vo.AppEnvV1;
import com.clsaa.dop.server.application.model.vo.ClusterInfoV1;
import com.clsaa.dop.server.application.service.AppEnvService;
import com.clsaa.dop.server.application.service.KubeCredentialService;
import com.clsaa.dop.server.application.service.KubeYamlService;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.K;
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
    @Autowired
    KubeYamlService kubeYamlService;
    @Autowired
    KubeCredentialService kubeCredentialService;

    @ApiOperation(value = "查询应用环境信息", notes = "根据应用ID查询应用环境变量")
    @GetMapping(value = "/app/{appId}/allEnv")
    public List<AppEnvV1> findEnvironmentByAppId(
            @ApiParam(value = "appId", name = "应用ID", required = true) @PathVariable(value = "appId") Long appId) {
        return this.appEnvService.findEnvironmentByAppId(appId).stream().map(l -> BeanUtils.convertType(l, AppEnvV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "查询应用环境详情", notes = "根据应用环境信息ID查询应用环境详情")
    @GetMapping(value = "/app/env/{appEnvId}")
    public AppEnvK8sV1 findEnvironmentDetailById(
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        AppEnvBoV1 appEnvBoV1 = this.appEnvService.findEnvironmentDetailById(appEnvId);
        KubeCredentialBoV1 kubeCredential = kubeCredentialService.findByAppEnvId(appEnvId);
        KubeYamlDataBoV1 kubeYamlDataBoV1 = this.kubeYamlService.findYamlDataByEnvId(appEnvId);
        if (kubeYamlDataBoV1 == null
        ) {
            return null;
        }
        AppEnvK8sV1 appEnvK8sV1 = AppEnvK8sV1.builder()
                .id(appEnvId)
                .deploymentStrategy(appEnvBoV1.getDeploymentStrategy())
                .imageUrl(kubeYamlDataBoV1.getImageUrl())
                .nameSpace(kubeYamlDataBoV1.getNameSpace())
                .releaseStrategy(kubeYamlDataBoV1.getReleaseStrategy())
                .service(kubeYamlDataBoV1.getService())
                .targetClusterUrl(kubeCredential.getTargetClusterUrl())
                .yamlFilePath(kubeYamlDataBoV1.getYamlFilePath())
                .build();
        return appEnvK8sV1;
    }


    @ApiOperation(value = "删除应用环境", notes = "根据应用环境ID删除应用环境")
    @DeleteMapping(value = "/app/env/{appEnvId}")
    public void deleteEnvironmentById(
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        this.appEnvService.deleteEnvironmentById(appEnvId);
    }


    @ApiOperation(value = "根据应用id创建应用环境", notes = "根据应用id创建应用环境")
    @PostMapping(value = "/app/{appId}/env")
    public void createEnvironmentByAppId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appId", name = "appId", required = true) @PathVariable(value = "appId") Long appId,
            @ApiParam(name = "title", value = "环境名称", required = true) @RequestParam(value = "title") String title,
            @ApiParam(name = "environmentLevel", value = "环境级别", required = true) @RequestParam(value = "environmentLevel") String environmentLevel,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy) {
        this.appEnvService.createEnvironmentByAppId(appId, cuser, title, environmentLevel, deploymentStrategy);
    }

    @ApiOperation(value = "创建yaml信息", notes = "创建yaml信息")
    @PostMapping(value = "/app/env/{appEnvId}/yaml")
    public void CreateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", defaultValue = "") @RequestParam(value = "nameSpace", defaultValue = "") String nameSpace,
            @ApiParam(name = "service", value = "服务", defaultValue = "") @RequestParam(value = "service", defaultValue = "") String service,
            @ApiParam(name = "deployment", value = "部署", defaultValue = "") @RequestParam(value = "deployment", defaultValue = "") String deployment,
            @ApiParam(name = "containers", value = "容器", defaultValue = "") @RequestParam(value = "containers", defaultValue = "") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", defaultValue = "0") @RequestParam(value = "replicas", defaultValue = "0") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "imageUrl", value = "镜像地址", required = true) @RequestParam(value = "imageUrl") String imageUrl,
            @ApiParam(name = "yamlFilePath", value = "镜像地址", defaultValue = "") @RequestParam(value = "yamlFilePath", defaultValue = "") String yamlFilePath) throws Exception {
        this.appEnvService.CreateYamlInfoByAppEnvId(appEnvId, cuser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, imageUrl, yamlFilePath);
    }

    @ApiOperation(value = "更新流水线信息", notes = "更新流水线信息")
    @PutMapping(value = "/app/env/{appEnvId}/pipeline")
    public void UpdatePipelineByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "pipelineId", name = "pipelineId", required = true) @RequestParam(value = "pipelineId") String pipelineId
    ) {
        this.appEnvService.updatePipeline(cuser, appEnvId, pipelineId);
    }

    @ApiOperation(value = "获取流水线信息", notes = "获取流水线信息")
    @GetMapping(value = "/app/env/{appEnvId}/pipeline")
    public AppEnvPipelineV1 getPipelineByAppEnvId(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId
    ) {
        return BeanUtils.convertType(this.appEnvService.getPipeline(appEnvId), AppEnvPipelineV1.class);
    }

    @ApiOperation(value = "更新yaml信息", notes = "更新yaml信息")
    @PutMapping(value = "/app/env/{appEnvId}/yaml")
    public void UpdateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", defaultValue = "") @RequestParam(value = "nameSpace", defaultValue = "") String nameSpace,
            @ApiParam(name = "service", value = "服务", defaultValue = "") @RequestParam(value = "service", defaultValue = "") String service,
            @ApiParam(name = "deployment", value = "部署", defaultValue = "") @RequestParam(value = "deployment", defaultValue = "") String deployment,
            @ApiParam(name = "containers", value = "容器", defaultValue = "") @RequestParam(value = "containers", defaultValue = "") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", defaultValue = "0") @RequestParam(value = "replicas", defaultValue = "0") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "imageUrl", value = "镜像地址", required = true) @RequestParam(value = "imageUrl") String imageUrl,
            @ApiParam(name = "yamlFilePath", value = "镜像地址", defaultValue = "") @RequestParam(value = "yamlFilePath", defaultValue = "") String yamlFilePath) throws Exception {
        this.appEnvService.UpdateYamlInfoByAppEnvId(appEnvId, cuser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, imageUrl, yamlFilePath);
    }

    @ApiOperation(value = "判断yaml是否存在", notes = "判断yaml是否存在")
    @GetMapping(value = "/app/env/{appEnvId}/yamlStatus")
    public Boolean isExistYamlData(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return this.kubeYamlService.isExistYamlData(appEnvId);
    }

    @ApiOperation(value = "获取命名空间", notes = "获取命名空间")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allNamespaces")
    public List<String> getNameSpaceByUrlAndToken(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId
    ) {
        try {
            return this.appEnvService.findNameSpaces(appEnvId);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取服务", notes = "获取服务")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allServices")
    public List<String> getServiceByNameSpace(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace
    ) {
        try {
            return this.appEnvService.getServiceByNameSpace(appEnvId, namespace);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取部署", notes = "获取部署")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allDeployment")
    public HashMap<String, Object> getDeploymentByNameSpaceAndService(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "service", name = "service", required = true) @RequestParam(value = "service") String service
    ) {
        try {
            return this.appEnvService.getDeploymentByNameSpaceAndService(appEnvId, namespace, service);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "创建服务", notes = "创建服务")
    @PostMapping(value = "/app/env/{appEnvId}/cluster/service")
    public void createServiceByNameSpace(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "name", name = "name", required = true) @RequestParam(value = "name") String name,
            @ApiParam(value = "port", name = "port", required = true) @RequestParam(value = "port") Integer port
    ) {
        try {
            this.appEnvService.createServiceByNameSpace(appEnvId, namespace, name, port);
        } catch (Exception e) {
            System.out.print(e);
        }
    }


    @ApiOperation(value = "更新集群信息", notes = "更新集群信息")
    @PostMapping(value = "/app/env/{appEnvId}/cluster")
    public void updateUrlAndToken(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long muser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "clusterInfoV1", name = "clusterInfoV1", required = true) @RequestBody ClusterInfoV1 clusterInfoV1) {
        try {
            this.appEnvService.updateUrlAndToken(muser, appEnvId, clusterInfoV1.getTargetClusterUrl(), clusterInfoV1.getTargetClusterToken());
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @ApiOperation(value = "获取传输的yaml文件", notes = "获取传输的yaml文件")
    @GetMapping(value = "/app/env/{appEnvId}/yamlFile")
    public HashMap<String, String> createYamlFileForDeploy(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        try {
            return this.appEnvService.createYamlFileForDeploy(appEnvId);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }


}
