package com.clsaa.dop.server.pipeline.service;


import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.pipeline.dao.PipelineRepository;
import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.feign.ApplicationFeign;
import com.clsaa.dop.server.pipeline.feign.PipelineFeign;
import com.clsaa.dop.server.pipeline.feign.UserFeign;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV2;
import com.clsaa.dop.server.pipeline.model.bo.PipelineV1Project;
import com.clsaa.dop.server.pipeline.model.dto.AppBasicInfoV1;
import com.clsaa.dop.server.pipeline.model.dto.KubeCredentialWithTokenV1;
import com.clsaa.dop.server.pipeline.model.dto.UserCredential;
import com.clsaa.dop.server.pipeline.model.dto.UserCredentialV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVo;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV1;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV2;
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
    public String addPipeline(PipelineVoV1 pipelineV1, Long loginUser) {
        ObjectId id = new ObjectId();
        Pipeline pipeline = Pipeline.builder()
                .id(id)
                .name(pipelineV1.getName())
                .monitor(pipelineV1.getMonitor())
                .timing(pipelineV1.getTiming())
                .config(pipelineV1.getConfig())
                .appId(pipelineV1.getAppId())
                .appEnvId(pipelineV1.getAppEnvId())
                .stages(pipelineV1.getStages())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(loginUser)
                .isDeleted(false)
                .build();

        pipelineRepository.insert(pipeline);

        //拿到 result output id
        String resultOutputId = this.resultOutputService.create(id.toString());
        //拿到 校验流水线信息完整
        PipelineBoV1 pipelineBoV1 = this.setInfo(id.toString(), resultOutputId, loginUser);
        this.jenkinsService.createJob(pipelineBoV1, "1.0");
        return id.toString();
    }

    public void addPipelineWithJenkins(PipelineVoV2 pipelineV2, Long loginUser) {
        ObjectId id = new ObjectId();
        Pipeline pipeline = Pipeline.builder()
                .id(id)
                .name(pipelineV2.getName())
                .monitor(pipelineV2.getMonitor())
                .timing(pipelineV2.getTiming())
                .config(pipelineV2.getConfig())
                .appId(pipelineV2.getAppId())
                .appEnvId(pipelineV2.getAppEnvId())
                .jenkinsfile(pipelineV2.getJenkinsfile())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(loginUser)
                .isDeleted(false)
                .build();

        pipelineRepository.insert(pipeline);

        PipelineBoV1 pipelineBoV1 = PipelineBoV1.builder()
                .id(id.toString())
                .name(pipelineV2.getName())
                .monitor(pipelineV2.getMonitor())
                .timing(pipelineV2.getTiming())
                .config(pipelineV2.getConfig())
                .jenkinsfile(pipelineV2.getJenkinsfile())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(pipelineV2.getCuser())
                .isDeleted(false)
                .build();

        this.jenkinsService.createByJenkinsfile(pipelineBoV1.getId(), pipelineBoV1.getJenkinsfile().getGit(), pipelineBoV1.getJenkinsfile().getPath());
    }

    public List<PipelineVoV3> getPipelineForTable() {
        List<Pipeline> pipelines = this.pipelineRepository.findAllNoDeleted();
        List<PipelineVoV3> pipelineVoV3s = new ArrayList<>();
        for (int i = 0; i < pipelines.size(); i++) {
            PipelineVoV3 pipelineVoV3 = PipelineVoV3.builder()
                    .id(pipelines.get(i).getId().toString())
                    .name(pipelines.get(i).getName())
                    .cuser(this.userFeign.findUserByIdV1(pipelines.get(i).getCuser()).getName())
                    .ctime(pipelines.get(i).getCtime())
                    .build();
            pipelineVoV3s.add(pipelineVoV3);
        }
        return pipelineVoV3s;
    }

    /**
     * 获得全部流水线信息, 存在返回全部流水线信息，若不存在返回null
     */
    public List<PipelineBoV2> findAll() {
        List<Pipeline> pipelines = this.pipelineRepository.findAllNoDeleted();
        List<PipelineBoV2> pipelineBoV2s = new ArrayList<>();
        for (int i = 0; i < pipelines.size(); i++) {
            System.out.println(pipelines.get(i).getMonitor());
            PipelineBoV2 pipelineBoV2 = PipelineBoV2.builder()
                    .id(pipelines.get(i).getId().toString())
                    .name(pipelines.get(i).getName())
                    .monitor(pipelines.get(i).getMonitor().ordinal())
                    .timing(pipelines.get(i).getTiming())
                    .config(pipelines.get(i).getConfig().ordinal())
                    .appId(pipelines.get(i).getAppId())
                    .appEnvId(pipelines.get(i).getAppEnvId())
                    .jenkinsfile(pipelines.get(i).getJenkinsfile())
                    .stages(pipelines.get(i).getStages())
                    .ctime(pipelines.get(i).getCtime())
                    .mtime(pipelines.get(i).getMtime())
                    .cuser(pipelines.get(i).getCuser())
                    .build();
            pipelineBoV2s.add(pipelineBoV2);
        }

        return pipelineBoV2s;
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
                    .timing(pipeline.getTiming())
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
                .timing(pipelineBoV1.getTiming())
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

    public PipelineBoV1 setInfo(String pipelineId, String resultOutputId, Long loginUser) {
        PipelineBoV1 pipelineBoV1 = this.findById(new ObjectId(pipelineId));
        if (pipelineBoV1 != null && pipelineBoV1.getConfig().equals("无Jenkinsfile")) {
            String gitUrl = null;
            String dockerUserName = null;
            String dockerPassword = null;
            String repository = null;
            String repositoryVersion = null;
            String deploy = null;
            String ip = null;
            String token = null;
            //收集信息

            System.out.println(pipelineBoV1.getCuser());
            UserCredentialV1 userCredentialV1 = this.userFeign.getUserCredentialV1ByUserId(loginUser, UserCredential.Type.DOP_INNER_HARBOR_LOGIN_EMAIL);

            dockerUserName = userCredentialV1.getIdentifier();
            dockerPassword = userCredentialV1.getCredential();


            if (pipelineBoV1.getAppId() != null) {

                AppBasicInfoV1 appBasicInfoV1 = this.applicationFeign.findAppById(pipelineBoV1.getAppId());
                gitUrl = appBasicInfoV1.getWarehouseUrl();
                repository = appBasicInfoV1.getImageUrl();
            }

            if (pipelineBoV1.getAppEnvId() != null) {
                repositoryVersion = this.applicationFeign.findBuildTagByAppEnvIdAndRunningId(loginUser, pipelineBoV1.getAppEnvId(), resultOutputId);
            }

            if (repositoryVersion != null) {
                deploy = this.applicationFeign.createYamlFileForDeploy(loginUser, pipelineBoV1.getAppEnvId(), resultOutputId);
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
                KubeCredentialWithTokenV1 kubeCredentialWithTokenV1 = this.applicationFeign.getUrlAndTokenByAppEnvId(pipelineBoV1.getAppEnvId());
                if (kubeCredentialWithTokenV1 == null) {
                    ip = "https://121.43.191.226:6443";
                    token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC10b2tlbi1sY25kOCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImIyZDBlYTQzLTA5MzAtMTFlOS1hYmM3LTAwMTYzZTBlYzFjZiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZCJ9.KlrkaUDeoyWngUwbmGS2C7gpSixEYJYRgv52w9v_YVLe_uDO_SdHAaQanxG8W23RbKxYPRt_0S7haFy-gU5ngbuYPxHVvPMoB8gVrPX8dGOvYpxvs26eOEjibgnfJTmegWBgylSP9ULKqLTgJ3feFiUyMtd_metvaCSJInPDonDFlvNTzLIn8sOxE3Qxq3fAApNgkxNeuHT8vygznoLysv0I3Tzobhn5R78q5D1QL01AxRlAIKm57i6h5X7utoXrnt8JbuLlMk2ZERa8ANTlhTDhFOj4ODiAqWgN2gtDUmX9ACGHr7kbU8HW_COj4QMS6gLNdnI4bBxTCWVSL-er9Q";
                } else {
                    ip = kubeCredentialWithTokenV1.getTargetClusterUrl();
                    token = kubeCredentialWithTokenV1.getTargetClusterToken();
                }
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
                            task.setIp(ip);
                            task.setToken(token);
                            break;
                    }
                }
            }
        }
        return pipelineBoV1;

    }



}
