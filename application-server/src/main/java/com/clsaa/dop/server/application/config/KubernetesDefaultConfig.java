package com.clsaa.dop.server.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "k8s")
public class KubernetesDefaultConfig {
    private String deploymentApiVersion = "apps/v1beta1";
    private String deploymentKind = "Deployment";
    private String deploymentLabelPrefix = "app";
    private String deploymentContainerPortProtocol = "TCP";
    private String deploymentImagePullPolicy = "Always";
    private String deploymentLocalTimeMountPath = "/etc/localtime";
    private String deploymentLocalTimeMountName = "host-time";
    private String deploymentTimeZoneMountPath = "/etc/timezone";
    private String deploymentTimeZoneMountName = "host-timezone";
    private String deploymentDnsPolicy = "ClusterFirst";

}
