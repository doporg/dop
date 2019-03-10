package com.clsaa.dop.server.application.model.bo;

import com.clsaa.dop.server.application.model.po.AppEnvironment;
import lombok.*;

import javax.persistence.Column;


/**
 * 应用环境业务层对象
 *
 * @author Bowen
 * @since 2019-3-14
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppEnvBoV1 {

    private Long id;

    /**
     * 环境名称
     */
    private String title;

    /**
     * 目标集群Url
     */
    private String targetClusterUrl;

    /**
     * 目标集群Token
     */
    private String targetClusterToken;

    /**
     * 部署策略
     */
    private AppEnvironment.DeploymentStrategy deploymentStrategy;


    /**
     * 环境级别
     */
    private AppEnvironment.EnvironmentLevel environmentLevel;

}
