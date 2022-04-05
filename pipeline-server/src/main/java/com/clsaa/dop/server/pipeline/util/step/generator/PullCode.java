package com.clsaa.dop.server.pipeline.util.step.generator;

import com.clsaa.dop.server.pipeline.util.StepUtil;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PullCode {

    private static final List<String> steps = new ArrayList<>(Arrays.asList(
            "deleteDir()",
            "sh 'git clone \"${gitUrl}\"'"
    ));
    private static final List<String> stepsWithDirectory = new ArrayList<>(Arrays.asList(
            "sh 'echo commitId `git rev-parse HEAD`'"
    ));

    public static String generate(StepGenerationOption option) {
        List<String> steps = PullCode.steps;
        if (option.withDirectory()) {
            steps.addAll(StepUtil.wrapperWithDirectory(option.getDirectory(), stepsWithDirectory));
        }

        return StepUtil.generate(option.getValuesMap(), steps);
    }
}
