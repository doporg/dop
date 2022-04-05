package com.clsaa.dop.server.pipeline.util.step.generator;

import com.clsaa.dop.server.pipeline.util.StepUtil;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildDocker {

    private static final List<String> steps = new ArrayList<>(Arrays.asList(
            "sh 'docker build -t ${imageName}:${repositoryVersion} .'"
    ));

    public static String generate(StepGenerationOption option) {
        List<String> steps = BuildDocker.steps;
        if (option.withDirectory()) {
            steps = StepUtil.wrapperWithDirectory(option.getDirectory(), BuildDocker.steps);
        }

        return StepUtil.generate(option.getValuesMap(), steps);
    }
}
