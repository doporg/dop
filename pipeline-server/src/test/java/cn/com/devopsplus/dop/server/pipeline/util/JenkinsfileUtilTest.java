package cn.com.devopsplus.dop.server.pipeline.util;

import cn.com.devopsplus.dop.server.pipeline.config.Jenkinsfile;
import cn.com.devopsplus.dop.server.pipeline.enums.StepType;
import cn.com.devopsplus.dop.server.pipeline.model.po.Stage;
import cn.com.devopsplus.dop.server.pipeline.model.po.Step;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class JenkinsfileUtilTest {

    final Logger logger = LoggerFactory.getLogger(JenkinsfileUtilTest.class);

    @Test
    public void TestJenkinsfileGeneratorExceptDeploy() throws Exception {
        // old version jenkinsfile not equals new version on DeployOnKubernetes step
        ArrayList<Step> steps = getStepsCase();
        steps.remove(steps.size()-1);
        ArrayList<Stage> stages = wrapperStages(steps);

        String oldVersion = new Jenkinsfile(0L, stages).getScript();
        String newVersion = JenkinsfileUtil.generate(stages);       // yes, you need not provide `appEnvID`

        Function<String, String> removeAllWhitespace = x -> x
                .replace(" ", "")
                .replace(System.lineSeparator(), "");

        assertEquals(removeAllWhitespace.apply(oldVersion), removeAllWhitespace.apply(newVersion));
    }

    @Test
    public void TestJenkinsfileGenerator() throws Exception {
        // old version jenkinsfile not equals new version on DeployOnKubernetes step
        ArrayList<Step> steps = getStepsCase();
        ArrayList<Stage> stages = wrapperStages(steps);

        String oldVersion = new Jenkinsfile(0L, stages).getScript();
        String newVersion = JenkinsfileUtil.generate(stages);

        Function<String, String> removeAllWhitespace = x -> x
                .replace(" ", "")
                .replace(System.lineSeparator(), "");

        logger.info("[TestJenkinsfileGenerator] new version Jenkinsfile: file={}", newVersion);
        assertNotEquals(removeAllWhitespace.apply(oldVersion), removeAllWhitespace.apply(newVersion));
    }


    private ArrayList<Stage> wrapperStages(ArrayList<Step> steps) {
        ArrayList<Stage> stages = new ArrayList<>();
        for (Step step: steps) {
            ArrayList<Step> tmpSteps = new ArrayList<>();
            tmpSteps.add(step);
            Stage stage = Stage.builder()
                    .name(step.getTaskName().name())
                    .steps(tmpSteps)
                    .build();
            stages.add(stage);
        }

        return stages;
    }

    private ArrayList<Step> getStepsCase() throws IOException {
        String manifests = IOUtils.toString(
                Objects.requireNonNull(JenkinsfileUtilTest.class.getResource("/JenkinsfileTest_echo-service-manifest.yaml")),
                StandardCharsets.UTF_8
        );
        return new ArrayList<>(Arrays.asList(
                Step.builder()
                        .taskName(StepType.PullCode)
                        .gitUrl("http://gitee.com/chunxu-zhang/echo-service")
                        .build(),
                Step.builder()
                        .taskName(StepType.BuildMaven)
                        .build(),
                Step.builder()
                        .taskName(StepType.BuildDocker)
                        .dockerUserName("chunxu")
                        .dockerPassword("123456789ABCDEF")
                        .repositoryVersion("123456789ABCDEF")
                        .repository("172.29.7.157:85/demo/echo-service")
                        .build(),
                Step.builder()
                        .taskName(StepType.PushDocker)
                        .dockerUserName("chunxu")
                        .dockerPassword("123456789ABCDEF")
                        .repositoryVersion("123456789ABCDEF")
                        .repository("172.29.7.157:85/demo/echo-service")
                        .build(),
                Step.builder()
                        .taskName(StepType.Deploy)
                        .deploy(manifests)
                        .build()
        ));
    }
}
