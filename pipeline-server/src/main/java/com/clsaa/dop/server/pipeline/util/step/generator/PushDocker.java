package com.clsaa.dop.server.pipeline.util.step.generator;

import com.clsaa.dop.server.pipeline.util.StepUtil;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PushDocker {

    private static final List<String> steps = new ArrayList<>(Arrays.asList(
            "sh 'docker login -u \"${dockerUserName}\" -p \"${dockerPassword}\" ${dockerRepoHost}'",
            "sh 'docker push ${imageName}:${repositoryVersion}'"
    ));

    public static String generate(StepGenerationOption option) {
        List<String> steps = PushDocker.steps;
        if (option.withDirectory()) {
            steps = StepUtil.wrapperWithDirectory(option.getDirectory(), PushDocker.steps);
        }

        return StepUtil.generate(option.getValuesMap(), steps);
    }
}
