package cn.com.devopsplus.dop.server.pipeline.util.step.generator;

import cn.com.devopsplus.dop.server.pipeline.util.step.StepGenerationOption;

public class Default {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public static String generate(StepGenerationOption option) {
        return "echo 'no such step type'" + LINE_SEPARATOR +
               "echo 'please check out your pipeline stages definition'";
    }
}
