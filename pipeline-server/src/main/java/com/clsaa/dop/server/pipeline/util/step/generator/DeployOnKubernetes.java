package com.clsaa.dop.server.pipeline.util.step.generator;

import com.clsaa.dop.server.pipeline.util.StepUtil;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeployOnKubernetes {

    private final static List<String> steps = new ArrayList<>(Arrays.asList(
            "sh '''",
            // here `&lt;` means `<`, generated jenkinsfiles will be filled in an xml object
            "cat &lt;&lt;EOF | kubectl apply -f -",
            "${deploy}",
            "EOF",
            "'''"
    ));

    public static String generate(StepGenerationOption option) {
        return StepUtil.generate(option.getValuesMap(), steps);
    }
}
