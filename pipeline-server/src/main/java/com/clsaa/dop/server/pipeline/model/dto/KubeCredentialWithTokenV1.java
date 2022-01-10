package com.clsaa.dop.server.pipeline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KubeCredentialWithTokenV1 {
    /**
     * 目标集群Url
     */

    private String targetClusterUrl;

    private String targetClusterToken;
}
