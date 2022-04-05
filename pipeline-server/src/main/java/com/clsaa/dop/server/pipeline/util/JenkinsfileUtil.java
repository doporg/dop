package com.clsaa.dop.server.pipeline.util;

import com.clsaa.dop.server.pipeline.enums.StepType;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.dop.server.pipeline.util.step.StepGenerationOption;
import com.clsaa.dop.server.pipeline.util.step.StepGenerator;
import com.clsaa.dop.server.pipeline.util.step.generator.*;

import java.util.*;
import java.util.function.Function;

/**
 * jenkinsfile 生成工具
 *
 * @author Chunxu Zhang
 * @since 2022-03-27
 */
public class JenkinsfileUtil {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final Map<StepType, StepGenerator> stepGenerators = new HashMap<>();

    static {
        stepGenerators.put(StepType.PullCode, PullCode::generate);
        stepGenerators.put(StepType.BuildMaven, BuildMaven::generate);
        stepGenerators.put(StepType.BuildNode, BuildNode::generate);
        stepGenerators.put(StepType.BuildDjango, BuildDjango::generate);
        stepGenerators.put(StepType.BuildDocker, BuildDocker::generate);
        stepGenerators.put(StepType.PushDocker, PushDocker::generate);
        stepGenerators.put(StepType.CustomScript, CustomScript::generate);
        stepGenerators.put(StepType.Deploy, DeployOnKubernetes::generate);
    }

    public static String generate(ArrayList<Stage> pipelineStages) {
        StringBuilder stagesBuilder = new StringBuilder();
        // this directory will be parsed in PullCode step, and used in all next steps
        String directory = null;

        for (Stage stage : pipelineStages) {
            String stageHeader = String.format("stage('%s') {", stage.getName());
            stagesBuilder.append(stageHeader).append(LINE_SEPARATOR);

            for (Step step : stage.getSteps()) {
                StepGenerator stepGenerator = stepGenerators.getOrDefault(step.getTaskName(), Default::generate);
                StepGenerationOption option = StepGenerationOption.fromStepPlaceholder(step).withDirectory(directory);
                if (directory == null && option.withDirectory())
                    directory = option.getDirectory();

                stagesBuilder
                        .append("steps {").append(LINE_SEPARATOR)
                        .append(stepGenerator.generate(option)).append(LINE_SEPARATOR)
                        .append("}").append(LINE_SEPARATOR);
            }
            stagesBuilder.append("}").append(LINE_SEPARATOR);
        }

        String jenkinsfile = String.join(LINE_SEPARATOR,
                "pipeline {",
                "agent any",
                "stages {",
                stagesBuilder,
                "}",
                "}"
        );

        return indent(jenkinsfile);
    }

    private static String indent(String jenkinsfile) {
        StringBuilder builder = new StringBuilder();
        Function<Integer, String> indentWhitespace = count ->
                String.join("", Collections.nCopies(count * 2, " "));

        int count = 0;
        boolean isInQuotation = false;
        for (String line : jenkinsfile.split(LINE_SEPARATOR)) {
            // a line only contains whitespaces need to be dropped
            if (line.trim().isEmpty()) continue;

            if (line.contains("}"))
                count = Math.max(count - 1, 0);

            builder.append(indentWhitespace.apply(isInQuotation?0:count))
                    .append(line)
                    .append(LINE_SEPARATOR);

            if (line.contains("'''"))
                isInQuotation = !isInQuotation;
            if (line.contains("{"))
                count++;
        }

        return builder.toString();
    }
}
