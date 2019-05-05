package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.PipelineRepository;
import com.clsaa.dop.server.pipeline.feign.ApplicationFeign;
import com.clsaa.dop.server.pipeline.feign.UserFeign;
import com.clsaa.dop.server.pipeline.model.bo.PipelineV1Project;
import com.clsaa.dop.server.pipeline.model.dto.AppBasicInfoV1;
import com.clsaa.dop.server.pipeline.model.dto.KubeCredentialWithTokenV1;
import com.clsaa.dop.server.pipeline.model.dto.UserCredential;
import com.clsaa.dop.server.pipeline.model.dto.UserCredentialV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV3;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ResultOutputService resultOutputService;

    @Autowired
    private ApplicationFeign applicationFeign;

    @Autowired
    private UserFeign userFeign;

    /**
     * 添加流水线信息
     */
    public String addPipeline(Pipeline pipeline, Long loginUser) {
        ObjectId id = new ObjectId();
        pipeline.setId(id.toString());
        pipeline.setCuser(loginUser);
        pipeline.setCtime(LocalDateTime.now());
        pipeline.setMtime(LocalDateTime.now());
        pipeline.setIsDeleted(false);
        pipelineRepository.insert(pipeline);

        //拿到 result output id
        String resultOutputId = this.resultOutputService.create(id.toString());
        //拿到 校验流水线信息完整
        Pipeline pipelineWithInfo = this.setInfo(id.toString(), resultOutputId, loginUser);
        this.jenkinsService.createJob(pipelineWithInfo);
        return id.toString();
    }

    public void addPipelineWithJenkins(Pipeline pipeline, Long loginUser) {
        ObjectId id = new ObjectId();
        pipeline.setId(id.toString());
        pipeline.setCuser(loginUser);
        pipeline.setCtime(LocalDateTime.now());
        pipeline.setMtime(LocalDateTime.now());
        pipeline.setIsDeleted(false);
        pipelineRepository.insert(pipeline);

        this.jenkinsService.createByJenkinsfile(pipeline);
    }

    public List<PipelineVoV3> getPipelineForTable() {
        List<Pipeline> pipelines = this.pipelineRepository.findAllNoDeleted();
        List<PipelineVoV3> pipelineVoV3s = new ArrayList<>();
        for (int i = 0; i < pipelines.size(); i++) {
            PipelineVoV3 pipelineVoV3 = PipelineVoV3.builder()
                    .id(pipelines.get(i).getId())
                    .name(pipelines.get(i).getName())
                    .cuser(this.userFeign.findUserByIdV1(pipelines.get(i).getCuser()).getName())
                    .ctime(pipelines.get(i).getCtime())
                    .build();
            pipelineVoV3s.add(pipelineVoV3);
        }
        return pipelineVoV3s;
    }

    /**
     * 根据id进行逻辑删除
     */
    public Pipeline deleteById(String id) {
        Pipeline pipeline = this.pipelineRepository.findById(id);
        pipeline.setIsDeleted(true);
        this.pipelineRepository.save(pipeline);
        return pipeline;
    }
    /**
     * 获得全部流水线信息, 存在返回全部流水线信息，若不存在返回null
     */
    public List<Pipeline> findAll() {
        return this.pipelineRepository.findAllNoDeleted();
    }

    /**
     * 根据id查找，存在返回这条流水线信息，不存在返回null
     */
    public Pipeline findById(String id) {
        return this.pipelineRepository.findById(id);
    }

    /**
     * 更新流水线信息
     */
    public void update(Pipeline pipeline) {
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

    public Pipeline setInfo(String pipelineId, String resultOutputId, Long loginUser) {
        Pipeline pipeline = this.pipelineRepository.findById(pipelineId);
        if (pipeline != null && pipeline.getConfig().equals(Pipeline.Config.NoJenkinsfile)) {
            String gitUrl = null;
            String dockerUserName = null;
            String dockerPassword = null;
            String repository = null;
            String repositoryVersion = null;
            String deploy = null;
            String ip = null;
            String token = null;
            //收集信息

            UserCredentialV1 userCredentialV1 = this.userFeign.getUserCredentialV1ByUserId(loginUser, UserCredential.Type.DOP_INNER_HARBOR_LOGIN_EMAIL);

            dockerUserName = userCredentialV1.getIdentifier();
            dockerPassword = userCredentialV1.getCredential();


            if (pipeline.getAppId() != null) {
                AppBasicInfoV1 appBasicInfoV1 = this.applicationFeign.findAppById(pipeline.getAppId());
                gitUrl = appBasicInfoV1.getWarehouseUrl();
                repository = appBasicInfoV1.getImageUrl();
            }

            if (pipeline.getAppEnvId() != null) {
                repositoryVersion = this.applicationFeign.findBuildTagByAppEnvIdAndRunningId(loginUser, pipeline.getAppEnvId(), resultOutputId);
            }

            if (repositoryVersion != null) {
                deploy = this.applicationFeign.createYamlFileForDeploy(loginUser, pipeline.getAppEnvId(), resultOutputId);
                if (deploy == null) {
                    deploy = "apiVersion: extensions/v1beta1\n" +
                            "kind: Deployment\n" +
                            "metadata:\n" +
                            "  name: pipeline-server\n" +
                            "  namespace: dop\n" +
                            "spec:\n" +
                            "  replicas: 1\n" +
                            "  template:\n" +
                            "    metadata:\n" +
                            "      labels:\n" +
                            "        app: pipeline-server\n" +
                            "    spec:\n" +
                            "      containers:\n" +
                            "        - name: pipeline-server\n" +
                            "          imagePullPolicy: Always\n" +
                            "          image: registry.dop.clsaa.com/dop/pipeline-server:latest\n" +
                            "          resources:\n" +
                            "            requests:\n" +
                            "              memory: 384Mi\n" +
                            "              cpu: 250m\n" +
                            "            limits:\n" +
                            "              memory: 384Mi\n" +
                            "              cpu: 500m\n" +
                            "          volumeMounts:\n" +
                            "            - name: host-time\n" +
                            "              mountPath: /etc/localtime\n" +
                            "            - name: host-timezone\n" +
                            "              mountPath: /etc/timezone\n" +
                            "          ports:\n" +
                            "            - containerPort: 13600\n" +
                            "      volumes:\n" +
                            "        - name: host-time\n" +
                            "          hostPath:\n" +
                            "            path: /etc/localtime\n" +
                            "        - name: host-timezone\n" +
                            "          hostPath:\n" +
                            "            path: /etc/timezone";
                }
                KubeCredentialWithTokenV1 kubeCredentialWithTokenV1 = this.applicationFeign.getUrlAndTokenByAppEnvId(pipeline.getAppEnvId());
                if (kubeCredentialWithTokenV1 == null) {
                    ip = "https://121.43.191.226:6443";
                    token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC10b2tlbi1sY25kOCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImIyZDBlYTQzLTA5MzAtMTFlOS1hYmM3LTAwMTYzZTBlYzFjZiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZCJ9.KlrkaUDeoyWngUwbmGS2C7gpSixEYJYRgv52w9v_YVLe_uDO_SdHAaQanxG8W23RbKxYPRt_0S7haFy-gU5ngbuYPxHVvPMoB8gVrPX8dGOvYpxvs26eOEjibgnfJTmegWBgylSP9ULKqLTgJ3feFiUyMtd_metvaCSJInPDonDFlvNTzLIn8sOxE3Qxq3fAApNgkxNeuHT8vygznoLysv0I3Tzobhn5R78q5D1QL01AxRlAIKm57i6h5X7utoXrnt8JbuLlMk2ZERa8ANTlhTDhFOj4ODiAqWgN2gtDUmX9ACGHr7kbU8HW_COj4QMS6gLNdnI4bBxTCWVSL-er9Q";
                } else {
                    ip = kubeCredentialWithTokenV1.getTargetClusterUrl();
                    token = kubeCredentialWithTokenV1.getTargetClusterToken();
                }
            }

            List<Stage> stages = pipeline.getStages();
            for (int i = 0; i < stages.size(); i++) {
                List<Step> steps = stages.get(i).getSteps();
                for (int j = 0; j < steps.size(); j++) {
                    Step task = steps.get(j);
                    Step.TaskType taskName = task.getTaskName();
                    switch (taskName) {
                        case PullCode:
                            task.setGitUrl(gitUrl == null ? task.getGitUrl() : gitUrl);
                            break;
                        case BuildDocker:
                            task.setDockerUserName(dockerUserName == null ? task.getDockerUserName() : dockerUserName);
                            task.setRepository(repository == null ? task.getRepository() : repository);
                            task.setRepositoryVersion(repositoryVersion == null ? task.getRepositoryVersion() : repositoryVersion);
                            break;
                        case PushDocker:
                            task.setDockerUserName(dockerUserName == null ? task.getDockerUserName() : dockerUserName);
                            task.setRepository(repository == null ? task.getRepository() : repository);
                            task.setRepositoryVersion(repositoryVersion == null ? task.getRepositoryVersion() : repositoryVersion);
                            task.setDockerPassword(dockerPassword == null ? task.getDockerPassword() : dockerPassword);
                            break;
                        case Deploy:
                            task.setDeploy(deploy);
                            task.setIp(ip);
                            task.setToken(token);
                            break;
                    }
                }
            }
        }
        return pipeline;
    }



}
