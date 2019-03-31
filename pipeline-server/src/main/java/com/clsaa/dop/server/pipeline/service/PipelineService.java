package com.clsaa.dop.server.pipeline.service;


import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.pipeline.dao.PipelineRepository;
import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.feign.ApplicationFeign;
import com.clsaa.dop.server.pipeline.feign.PipelineFeign;
import com.clsaa.dop.server.pipeline.feign.UserFeign;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.bo.PipelineV1Project;
import com.clsaa.dop.server.pipeline.model.dto.AppBasicInfoV1;
import com.clsaa.dop.server.pipeline.model.dto.UserCredential;
import com.clsaa.dop.server.pipeline.model.dto.UserCredentialV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV1;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV2;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 流水线信息业务实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */
@Service
public class PipelineService {
    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private JenkinsService jenkinsService;


    @Autowired
    private ApplicationFeign applicationFeign;

    @Autowired
    private UserFeign userFeign;

    /**
     * 添加流水线信息
     */
    public String addPipeline(PipelineVoV1 pipelineV1) {
        ObjectId id = new ObjectId();
        Pipeline pipeline = Pipeline.builder()
                .id(id)
                .name(pipelineV1.getName())
                .monitor(pipelineV1.getMonitor())
                .config(pipelineV1.getConfig())
                .appId(pipelineV1.getAppId())
                .appEnvId(pipelineV1.getAppEnvId())
                .stages(pipelineV1.getStages())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(pipelineV1.getCuser())
                .isDeleted(false)
                .build();

        pipelineRepository.insert(pipeline);
        return id.toString();

    }

    public void addPipelineWithJenkins(PipelineVoV2 pipelineV2) {
        ObjectId id = new ObjectId();
        Pipeline pipeline = Pipeline.builder()
                .id(id)
                .name(pipelineV2.getName())
                .monitor(pipelineV2.getMonitor())
                .config(pipelineV2.getConfig())
                .appId(pipelineV2.getAppId())
                .appEnvId(pipelineV2.getAppEnvId())
                .jenkinsfile(pipelineV2.getJenkinsfile())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(pipelineV2.getCuser())
                .isDeleted(false)
                .build();

        pipelineRepository.insert(pipeline);

        PipelineBoV1 pipelineBoV1 = PipelineBoV1.builder()
                .id(id.toString())
                .name(pipelineV2.getName())
                .monitor(pipelineV2.getMonitor())
                .config(pipelineV2.getConfig())
                .jenkinsfile(pipelineV2.getJenkinsfile())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(pipelineV2.getCuser())
                .isDeleted(false)
                .build();

        this.jenkinsService.createByJenkinsfile(pipelineBoV1.getId(), pipelineBoV1.getJenkinsfile().getGit(), pipelineBoV1.getJenkinsfile().getPath());
    }

    /**
     * 获得全部流水线信息, 存在返回全部流水线信息，若不存在返回null
     */
    public List<PipelineBoV1> findAll() {
        List<Pipeline> pipelines = this.pipelineRepository.findAll();
        List<PipelineBoV1> pipelineBoV1s = new ArrayList<PipelineBoV1>();
        for (int i = 0; i < pipelines.size(); i++) {
            PipelineBoV1 pipelineBoV1 = PipelineBoV1.builder()
                    .id(pipelines.get(i).getId().toString())
                    .name(pipelines.get(i).getName())
                    .monitor(pipelines.get(i).getMonitor())
                    .config(pipelines.get(i).getConfig())
                    .appId(pipelines.get(i).getAppId())
                    .appEnvId(pipelines.get(i).getAppEnvId())
                    .jenkinsfile(pipelines.get(i).getJenkinsfile())
                    .stages(pipelines.get(i).getStages())
                    .ctime(pipelines.get(i).getCtime())
                    .mtime(pipelines.get(i).getMtime())
                    .cuser(pipelines.get(i).getCuser())
                    .isDeleted(pipelines.get(i).getIsDeleted())
                    .build();
            pipelineBoV1s.add(pipelineBoV1);
        }

        return pipelineBoV1s;
    }

    /**
     * 根据id进行逻辑删除
     */
    public void deleteById(String id) {
        PipelineBoV1 pipelineBoV1 = this.findById(new ObjectId(id));
        Pipeline pipeline = Pipeline.builder()
                .id(new ObjectId(id))
                .name(pipelineBoV1.getName())
                .monitor(pipelineBoV1.getMonitor())
                .stages(pipelineBoV1.getStages())
                .ctime(pipelineBoV1.getCtime())
                .mtime(pipelineBoV1.getMtime())
                .cuser(pipelineBoV1.getCuser())
                .isDeleted(true)
                .build();
        this.pipelineRepository.save(pipeline);
    }

    /**
     * 根据id查找，存在返回这条流水线信息，不存在返回null
     */
    public PipelineBoV1 findById(ObjectId id) {
        Optional<Pipeline> optionalPipeline = this.pipelineRepository.findById(id);
        if (optionalPipeline.isPresent()) {
            Pipeline pipeline = optionalPipeline.get();
            PipelineBoV1 pipelineBoV1 = PipelineBoV1.builder()
                    .id(pipeline.getId().toString())
                    .name(pipeline.getName())
                    .monitor(pipeline.getMonitor())
                    .appId(pipeline.getAppId())
                    .appEnvId(pipeline.getAppEnvId())
                    .config(pipeline.getConfig())
                    .jenkinsfile(pipeline.getJenkinsfile())
                    .stages(pipeline.getStages())
                    .ctime(pipeline.getCtime())
                    .mtime(pipeline.getMtime())
                    .cuser(pipeline.getCuser())
                    .isDeleted(pipeline.getIsDeleted())
                    .build();
            return pipelineBoV1;
        } else {
            return null;
        }
    }

    /**
     * 更新流水线信息
     */
    public void update(PipelineBoV1 pipelineBoV1) {
        Pipeline pipeline = Pipeline.builder()
                .id(new ObjectId(pipelineBoV1.getId()))
                .name(pipelineBoV1.getName())
                .monitor(pipelineBoV1.getMonitor())
                .config(pipelineBoV1.getConfig())
                .jenkinsfile(pipelineBoV1.getJenkinsfile())
                .stages(pipelineBoV1.getStages())
                .ctime(pipelineBoV1.getCtime())
                .mtime(pipelineBoV1.getMtime())
                .cuser(pipelineBoV1.getCuser())
                .appEnvId(pipelineBoV1.getAppEnvId())
                .appId(pipelineBoV1.getAppId())
                .isDeleted(false)
                .build();
        pipelineRepository.save(pipeline);
    }

    /**
     * 根据用户id查找，返回该用户的流水线信息
     */
    public List<PipelineV1Project> getPipelineById(Long cuser) {
        List<Pipeline> pipelines = this.pipelineRepository.findByCuser(cuser);
        List<PipelineV1Project> pipelineV1Projects = new ArrayList<>();
        for (int i = 0; i < pipelines.size(); i++) {
            if (!pipelines.get(i).getIsDeleted()) {
                PipelineV1Project pipelineV1Project = PipelineV1Project.builder()
                        .id(pipelines.get(i).getId().toString())
                        .name(pipelines.get(i).getName())
                        .ctime(pipelines.get(i).getCtime())
                        .cuser(pipelines.get(i).getCuser())
                        .build();
                pipelineV1Projects.add(pipelineV1Project);
            }
        }
        return pipelineV1Projects;
    }

    /**
     * 根据envid， 查询pipelineid
     */
    public List<PipelineV1Project> getPipelineIdByEnvId(Long envid) {
        System.out.println(envid);
        List<Pipeline> pipelines = this.pipelineRepository.findByAppEnvId(envid);
        List<PipelineV1Project> pipelineV1Projects = new ArrayList<>();
        for (int i = 0; i < pipelines.size(); i++) {
            if (!pipelines.get(i).getIsDeleted()) {
                PipelineV1Project pipelineV1Project = PipelineV1Project.builder()
                        .id(pipelines.get(i).getId().toString())
                        .name(pipelines.get(i).getName())
                        .ctime(pipelines.get(i).getCtime())
                        .cuser(pipelines.get(i).getCuser())
                        .build();
                pipelineV1Projects.add(pipelineV1Project);
            }
        }
        return pipelineV1Projects;
    }

    public PipelineBoV1 setInfo(String pipelineId, String resultOutputId) {
        PipelineBoV1 pipelineBoV1 = this.findById(new ObjectId(pipelineId));
        if (pipelineBoV1 != null && pipelineBoV1.getConfig().equals("无Jenkinsfile")) {
            String gitUrl = null;
            String dockerUserName = null;
            String dockerPassword = null;
            String repository = null;
            String repositoryVersion = null;
            String deploy = null;
            //收集信息

            System.out.println(pipelineBoV1.getCuser());
            UserCredentialV1 userCredentialV1 = this.userFeign.getUserCredentialV1ByUserId(pipelineBoV1.getCuser(), UserCredential.Type.DOP_INNER_HARBOR_LOGIN_EMAIL);

            dockerUserName = userCredentialV1.getIdentifier();
            dockerPassword = userCredentialV1.getCredential();


            if(pipelineBoV1.getAppId() != null){

                AppBasicInfoV1 appBasicInfoV1 = this.applicationFeign.findAppById(pipelineBoV1.getAppId());
                gitUrl = appBasicInfoV1.getWarehouseUrl();
                repository = appBasicInfoV1.getImageUrl();
            }

            if(pipelineBoV1.getAppEnvId() != null){
                repositoryVersion = this.applicationFeign.findBuildTagByAppEnvIdAndRunningId(pipelineBoV1.getCuser(), pipelineBoV1.getAppEnvId(), resultOutputId);
            }

            if(repositoryVersion != null){
                repositoryVersion = this.applicationFeign.findBuildTagByAppEnvIdAndRunningId(pipelineBoV1.getCuser(), pipelineBoV1.getAppEnvId(), resultOutputId);
            }

            List<Stage> stages = pipelineBoV1.getStages();
            for (int i = 0; i < stages.size(); i++) {
                List<Step> steps = stages.get(i).getSteps();
                for (int j = 0; j < steps.size(); j++) {
                    Step task = steps.get(j);
                    String taskName = task.getTaskName();
                    switch (taskName) {
                        case ("拉取代码"):
                            task.setGitUrl(gitUrl == null ? task.getGitUrl() : gitUrl);
                            break;
                        case ("构建docker镜像"):
                            task.setDockerUserName(dockerUserName == null ? task.getDockerUserName() : dockerUserName);
                            task.setRepository(repository == null ? task.getRepository() : repository);
                            task.setRepositoryVersion(repositoryVersion == null ? task.getRepositoryVersion() : repositoryVersion);
                            break;
                        case ("推送docker镜像"):
                            task.setDockerUserName(dockerUserName == null ? task.getDockerUserName() : dockerUserName);
                            task.setRepository(repository == null ? task.getRepository() : repository);
                            task.setRepositoryVersion(repositoryVersion == null ? task.getRepositoryVersion() : repositoryVersion);
                            task.setDockerPassword(dockerPassword == null ? task.getDockerPassword() : dockerPassword);
                            break;
                        case ("部署"):
                            task.setDeploy(deploy);
                            break;
                    }
                }
            }
        }
        return pipelineBoV1;

    }

}
