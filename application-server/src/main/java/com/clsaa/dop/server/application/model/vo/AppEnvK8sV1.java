package com.clsaa.dop.server.application.model.vo;

import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.po.KubeYamlData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * 应用环境视图层对象
 *
 * @author Bowen
 * @since 2019-3-14
 **/
@Getter
@Setter
@Builder
public class AppEnvK8sV1 {

    private Long id;


    /**
     * 目标集群Url
     */

    private String targetClusterUrl;


    /**
     * 镜像地址
     */
    private String imageUrl;

    /**
     * 命名空间
     */
    private String nameSpace;

    /**
     * 服务
     */
    private String service;

    /**
     * 发布策略
     */
    private KubeYamlData.ReleaseStrategy releaseStrategy;


    /**
     * 部署策略
     */
    private AppEnvironment.DeploymentStrategy deploymentStrategy;

}
