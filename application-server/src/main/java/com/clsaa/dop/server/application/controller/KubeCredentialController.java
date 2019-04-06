package com.clsaa.dop.server.application.controller;


import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.ClusterInfoV1;
import com.clsaa.dop.server.application.model.vo.ClusterUrlV1;
import com.clsaa.dop.server.application.model.vo.KubeCredentialWithTokenV1;
import com.clsaa.dop.server.application.service.KubeCredentialService;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 集群信息接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-25
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class KubeCredentialController {
    @Autowired
    KubeCredentialService kubeCredentialService;

    @ApiOperation(value = "更新集群信息", notes = "更新集群信息")
    @PostMapping(value = "/app/env/{appEnvId}/cluster")
    public void updateUrlAndToken(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long muser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "clusterInfoV1", name = "clusterInfoV1", required = true) @RequestBody ClusterInfoV1 clusterInfoV1) {
        try {
            this.kubeCredentialService.updateClusterInfo(muser, appEnvId, clusterInfoV1.getTargetClusterUrl(), clusterInfoV1.getTargetClusterToken());
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @ApiOperation(value = "查询集群url", notes = "查询集群url")
    @GetMapping(value = "/app/env/{appEnvId}/clusterUrl")
    public ClusterUrlV1 getUrlByAppEnvId(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        try {
            return BeanUtils.convertType(this.kubeCredentialService.findByAppEnvId(appEnvId), ClusterUrlV1.class);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "查询集群url和Token", notes = "查询集群url和Token")
    @GetMapping(value = "/app/env/{appEnvId}/clusterWithToken")
    public KubeCredentialWithTokenV1 getUrlAndTokenByAppEnvId(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        try {
            return BeanUtils.convertType(this.kubeCredentialService.findByAppEnvId(appEnvId), KubeCredentialWithTokenV1.class);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

}
