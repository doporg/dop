package com.clsaa.dop.server.pipeline.util.step.generator;

import com.clsaa.dop.server.pipeline.util.StepUtil;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;

import java.util.Arrays;
import java.util.List;

public class CustomScript {
    public static String generate(StepGenerationOption option) {
        List<String> steps = Arrays.asList("sh '${shell}'");
        return StepUtil.generate(option.getValuesMap(), steps);
    }
}
