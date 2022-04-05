package com.clsaa.dop.server.pipeline.util.step.generator;

import com.clsaa.dop.server.pipeline.util.StepUtil;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildMaven {

    private static final List<String> steps = new ArrayList<>(Arrays.asList(
            "sh 'mvn --version'",
            "sh 'mvn -U -am clean package'"
    ));

    public static String generate(StepGenerationOption option) {
        List<String> steps = BuildMaven.steps;
        if (option.withDirectory()) {
            steps = StepUtil.wrapperWithDirectory(option.getDirectory(), BuildMaven.steps);
        }

        return StepUtil.generate(option.getValuesMap(), steps);
    }
}
