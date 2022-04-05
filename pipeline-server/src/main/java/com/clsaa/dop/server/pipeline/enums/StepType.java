package com.clsaa.dop.server.pipeline.enums;

public enum StepType {
    PullCode,
    BuildMaven,
    BuildNode,
    BuildDjango,
    BuildDocker,
    PushDocker,
    CustomScript,
    Deploy
}