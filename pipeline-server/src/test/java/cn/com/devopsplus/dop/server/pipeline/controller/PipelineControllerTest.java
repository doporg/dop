package cn.com.devopsplus.dop.server.pipeline.controller;

import cn.com.devopsplus.dop.server.pipeline.feign.UserFeign;
import cn.com.devopsplus.dop.server.pipeline.model.dto.UserV1;
import cn.com.devopsplus.dop.server.pipeline.model.po.Jenkinsfile;
import cn.com.devopsplus.dop.server.pipeline.model.po.Pipeline;
import cn.com.devopsplus.dop.server.pipeline.model.vo.PipelineVoV3;
import cn.com.devopsplus.dop.server.pipeline.util.JenkinsfileUtilTest;
import com.jayway.jsonpath.JsonPath;
import com.offbytwo.jenkins.JenkinsServer;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ActiveProfiles(value = "test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureDataMongo
public class PipelineControllerTest {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PipelineController pipelineController;

    @MockBean
    private JenkinsServer jenkinsServer;
    @MockBean
    private UserFeign userFeign;

    @Before
    public void initMock() throws IOException {
        Function<String, UserV1> userWithName = name -> {
            UserV1 user = new UserV1();
            user.setName(name);
            return user;
        };

        when(jenkinsServer.getJob(anyString())).thenReturn(null);
        when(userFeign.findUserByIdV1(14L)).thenReturn(userWithName.apply("chunxu-zhang"));
        when(userFeign.findUserByIdV1(12L)).thenReturn(userWithName.apply("yuyan-yang"));
    }

    @Test
    public void testAddPipeline_Success() throws IOException {
        // get the json file
        String addPipeline = IOUtils.toString(
                Objects.requireNonNull(JenkinsfileUtilTest.class.getResource("/controller/PipelineController/addPipeline_Success.json")),
                StandardCharsets.UTF_8
        );
        Long id=Long.parseLong(JsonPath.read(addPipeline, "$.request.id"));
        String name = JsonPath.read(addPipeline, "$.request.name");
        String git = JsonPath.read(addPipeline, "$.request.jenkinsfile.git");
        String path = JsonPath.read(addPipeline, "$.request.jenkinsfile.path");
        Jenkinsfile jenkinsfile = Jenkinsfile.builder()
                .git(git)
                .path(path)
                .build();

        Pipeline pipeline = Pipeline.builder()
                .name(name)
                .monitor(Pipeline.Monitor.ManualTrigger)
                .config(Pipeline.Config.HasJenkinsfile)
                .jenkinsfile(jenkinsfile)
                .build();

        pipelineController.addPipeline(id, pipeline);
    }

    @Test
    public void testGetPipelineForTable_Success() throws IOException {
        // get the input json file
        String addPipeline = IOUtils.toString(
                Objects.requireNonNull(JenkinsfileUtilTest.class.getResource("/controller/PipelineController/addPipeline_Success.json")),
                StandardCharsets.UTF_8
        );
        Long id=Long.parseLong(JsonPath.read(addPipeline, "$.request.id"));
        String name = JsonPath.read(addPipeline, "$.request.name");
        String git = JsonPath.read(addPipeline, "$.request.jenkinsfile.git");
        String path = JsonPath.read(addPipeline, "$.request.jenkinsfile.path");
        // create a pipeline
        Jenkinsfile jenkinsfile = Jenkinsfile.builder()
                .git(git)
                .path(path)
                .build();

        Pipeline pipeline = Pipeline.builder()
                .name(name)
                .monitor(Pipeline.Monitor.ManualTrigger)
                .config(Pipeline.Config.HasJenkinsfile)
                .jenkinsfile(jenkinsfile)
                .build();

        pipelineController.addPipeline(id, pipeline);

        // get pipelines
        List<PipelineVoV3> pipelines = pipelineController.getPipelineForTable();

        Set<String> pipelineNames = pipelines.stream()
                .map(PipelineVoV3::getName)
                .collect(Collectors.toSet());
        Set<String> usernames = pipelines.stream()
                .map(PipelineVoV3::getCuser)
                .collect(Collectors.toSet());

        String getPipeline = IOUtils.toString(
                Objects.requireNonNull(JenkinsfileUtilTest.class.getResource("/controller/PipelineController/getPipelineForTable_Success.json")),
                StandardCharsets.UTF_8
        );
        int size=Integer.parseInt(JsonPath.read(getPipeline, "$.response.size"));
        name = JsonPath.read(getPipeline, "$.response.name");
        String username = JsonPath.read(getPipeline, "$.response.username");

        assertThat(pipelines).hasSize(size);
        assertThat(pipelineNames).contains(name);
        assertThat(usernames).contains(username);
    }
}
