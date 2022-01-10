package com.clsaa.dop.server.pipeline.model.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepVoV1 {

    private int taskName;

    private String gitUrl;

    private String dockerUserName;

    private String dockerPassword;

    private String repositoryVersion;

    private String repository;

    private String deploy;

    private String token;

    private String ip;

    private String shell;
}
