package com.clsaa.dop.server.application.config;

public interface KubernetesDefaultConfig {
    String deploymentApiVersion = "apps/v1beta1";
    String deploymentKind = "Deployment";
    String deploymentLabelPrefix = "app";
    String deploymentContainerPortProtocol = "TCP";
    String deploymentImagePullPolicy = "Always";
    String deploymentLocalTimeMountPath = "/etc/localtime";
    String deploymentLocalTimeMountName = "host-time";
    String deploymentTimeZoneMountPath = "/etc/timezone";
    String deploymentTimeZoneMountName = "host-timezone";
    String deploymentDnsPolicy = "ClusterFirst";

}
