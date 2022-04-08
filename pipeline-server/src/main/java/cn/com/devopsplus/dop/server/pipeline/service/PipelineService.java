package cn.com.devopsplus.dop.server.pipeline.service;


import cn.com.devopsplus.dop.server.pipeline.config.BizCodes;
import cn.com.devopsplus.dop.server.pipeline.dao.PipelineRepository;
import cn.com.devopsplus.dop.server.pipeline.enums.StepType;
import cn.com.devopsplus.dop.server.pipeline.feign.ApplicationFeign;
import cn.com.devopsplus.dop.server.pipeline.feign.UserFeign;
import cn.com.devopsplus.dop.server.pipeline.model.bo.PipelineV1Project;
import cn.com.devopsplus.dop.server.pipeline.model.dto.AppBasicInfoV1;
import cn.com.devopsplus.dop.server.pipeline.model.dto.KubeCredentialWithTokenV1;
import cn.com.devopsplus.dop.server.pipeline.model.dto.UserCredential;
import cn.com.devopsplus.dop.server.pipeline.model.dto.UserCredentialV1;
import cn.com.devopsplus.dop.server.pipeline.model.po.Pipeline;
import cn.com.devopsplus.dop.server.pipeline.model.po.Stage;
import cn.com.devopsplus.dop.server.pipeline.model.po.Step;
import cn.com.devopsplus.dop.server.pipeline.model.vo.PipelineVoV1;
import cn.com.devopsplus.dop.server.pipeline.model.vo.PipelineVoV3;
import cn.com.devopsplus.dop.server.pipeline.model.vo.StageVoV1;
import cn.com.devopsplus.dop.server.pipeline.model.vo.StepVoV1;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 添加流水线信息
     */
    public String addPipeline(Pipeline pipeline, Long loginUser) {
        logger.info("[addPipeline] Request coming: loginUser={}, pipeline",loginUser);
        ObjectId id = new ObjectId();
        pipeline.setId(id.toString());
        pipeline.setCuser(loginUser);
        pipeline.setCtime(LocalDateTime.now());
        pipeline.setMtime(LocalDateTime.now());
        pipeline.setIsDeleted(false);

        try {
            pipelineRepository.save(pipeline);
            //拿到 result output id
            String resultOutputId = this.resultOutputService.create(id.toString());
            logger.info("[addPipeline] Get the result output id: resultOutputId={}",resultOutputId);
            //拿到 校验流水线信息完整
            Pipeline pipelineWithInfo = this.setInfo(id.toString(), resultOutputId, loginUser);
            this.jenkinsService.createJob(pipelineWithInfo);
            return id.toString();
        } catch (Exception e) {
            logger.error("[addPipeline] 无法创建流水线信息！Exception",e);
            return e.toString();
        }
    }

    public void addPipelineWithJenkins(Pipeline pipeline, Long loginUser) {
        logger.info("[addPipelineWithJenkins] Request coming: loginUser={}, pipeline",loginUser);
        ObjectId id = new ObjectId();
        pipeline.setId(id.toString());
        pipeline.setCuser(loginUser);
        pipeline.setCtime(LocalDateTime.now());
        pipeline.setMtime(LocalDateTime.now());
        pipeline.setIsDeleted(false);
        pipelineRepository.save(pipeline);

        this.jenkinsService.createByJenkinsfile(pipeline);
    }

    public List<PipelineVoV3> getPipelineForTable() {
        logger.info("[getPipelineForTable] Request coming: ");
        List<Pipeline> pipelines = this.pipelineRepository.findAllNoDeleted();
        List<PipelineVoV3> pipelineVoV3s = new ArrayList<>();
        System.out.println();
        logger.info("[getPipelineForTable] Pipeline size is : {}",pipelines.size());
        for (int i = 0; i < pipelines.size(); i++) {
            logger.info("[getPipelineForTable] User id is : {}",pipelines.get(i).getCuser());
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
        logger.info("[deleteById] Request coming: id={}",id);
        Pipeline pipeline = this.pipelineRepository.findById(id);
        pipeline.setIsDeleted(true);
        this.pipelineRepository.save(pipeline);
        return pipeline;
    }

    /**
     * 获得全部流水线信息, 存在返回全部流水线信息，若不存在返回null
     */
    public List<Pipeline> findAll() {
        logger.info("[findAll] Request coming: ");
        return this.pipelineRepository.findAllNoDeleted();
    }

    /**
     * 根据id查找，存在返回这条流水线信息，不存在返回null
     */
    public Pipeline findById(String id) {
        logger.info("[findById] Request coming: id={}",id);
        return this.pipelineRepository.findById(id);
    }

    public PipelineVoV1 findByIdV1(String id) {
        logger.info("[findByIdV1] Request coming: id={}",id);
        Pipeline pipeline = this.pipelineRepository.findById(id);
        List<StageVoV1> stageVoV1s = new ArrayList<StageVoV1>();
        for (int i = 0; i < pipeline.getStages().size(); i++) {
            Stage stage = pipeline.getStages().get(i);
            List<StepVoV1> stepVoV1s = new ArrayList<StepVoV1>();
            for (int j = 0; j < stage.getSteps().size(); j++) {
                StepVoV1 stepVoV1 = StepVoV1.builder()
                        .taskName(stage.getSteps().get(j).getTaskName().ordinal())
                        .gitUrl(stage.getSteps().get(j).getGitUrl())
                        .dockerUserName(stage.getSteps().get(j).getDockerUserName())
                        .dockerPassword(stage.getSteps().get(j).getDockerPassword())
                        .repositoryVersion(stage.getSteps().get(j).getDockerPassword())
                        .repository(stage.getSteps().get(j).getRepository())
                        .deploy(stage.getSteps().get(j).getDeploy())
                        .token(stage.getSteps().get(j).getToken())
                        .ip(stage.getSteps().get(j).getIp())
                        .shell(stage.getSteps().get(j).getShell())
                        .build();
                stepVoV1s.add(stepVoV1);
            }
            StageVoV1 stageVoV1 = StageVoV1.builder()
                    .name(stage.getName())
                    .steps((ArrayList) stepVoV1s)
                    .build();
            stageVoV1s.add(stageVoV1);
        }

        PipelineVoV1 pipelineVoV1 = PipelineVoV1.builder()
                .id(pipeline.getId())
                .name(pipeline.getName())
                .monitor(pipeline.getMonitor().ordinal())
                .timing(pipeline.getTiming())
                .config(pipeline.getConfig().ordinal())
                .jenkinsfile(pipeline.getJenkinsfile())
                .stages((ArrayList) stageVoV1s)
                .appId(pipeline.getAppId())
                .appEnvId(pipeline.getAppEnvId()).build();
        return pipelineVoV1;
    }

    /**
     * 更新流水线信息
     */
    public void update(Pipeline pipeline) {
        logger.info("[update] Request coming: pipeline");
        pipelineRepository.save(pipeline);
    }

    /**
     * 根据用户id查找，返回该用户的流水线信息
     */
    public List<PipelineV1Project> getPipelineById(Long cuser) {
        logger.info("[PipelineV1Project] Request coming: cuser={}",cuser);
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
        logger.info("[PipelineV1Project] Request coming: envid={}",envid);
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
        logger.info("[setInfo] Request coming: pipelineId={}, resultOutputId={}, loginUser={}",pipelineId,resultOutputId,loginUser);
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

            UserCredentialV1 userCredentialV11 = this.userFeign.getUserCredentialV1ByUserId(pipeline.getCuser(), UserCredential.Type.DOP_INNER_HARBOR_LOGIN_EMAIL);
            BizAssert.found(userCredentialV11 != null, BizCodes.NOT_FOUND.getCode(), "无法获取docker账户");
            dockerUserName = userCredentialV11.getIdentifier();
            dockerPassword = userCredentialV11.getCredential();
            logger.info("[setInfo] Get the docker info: dockerUserName={}, dockerPassword={}",dockerUserName,dockerPassword);

            if (pipeline.getAppId() != null) {
                AppBasicInfoV1 appBasicInfoV1 = this.applicationFeign.findAppById(loginUser, pipeline.getAppId());
                gitUrl = appBasicInfoV1.getWarehouseUrl();
                repository = appBasicInfoV1.getImageUrl();
                logger.info("[setInfo] Get the git url and the image url: gitUrl={}, repository={}",gitUrl,repository);

                try {
                    if (gitUrl.startsWith("http")) {
                        String gitMatchHost = "172.29.7.157:8090";
                        String gitHost = new URL(gitUrl).getHost();
                        String gitPath = new URL(gitUrl).getPath();
                        if (gitHost.equals(gitMatchHost)) {
                            UserCredentialV1 userCredentialV12 = this.userFeign.getUserCredentialV1ByUserId(loginUser, UserCredential.Type.DOP_INNER_GITLAB_TOKEN);
                            String accessToken = userCredentialV12.getCredential();
                            gitUrl = "http://oauth2:" + accessToken + "@" + gitHost + gitPath;
                            logger.info("[setInfo] Get the git url: gitUrl={}",gitUrl);
                        }
                    } else {
//                        dockerRepoHost = respository.split("/")[0];
//                        imageName = respository;
                    }
                } catch (MalformedURLException e) {
                    logger.error("[setInfo] 无法获得gitlab仓库信息！Exception",e);
                    e.printStackTrace();
                    BizAssert.justFailed(new BizCode(BizCodes.INVALID_PARAM.getCode()
                            , e.getMessage()));
                }
            }

            if (pipeline.getAppEnvId() != null) {
                repositoryVersion = this.applicationFeign.findBuildTagByAppEnvIdAndRunningId(loginUser, pipeline.getAppEnvId(), resultOutputId);
            }
            logger.info("[setInfo] Get the repositoryVersion: repositoryVersion={}",repositoryVersion);

            if (repositoryVersion != null) {
                deploy = this.applicationFeign.createYamlFileForDeploy(loginUser, pipeline.getAppEnvId(), resultOutputId);
                BizAssert.found(deploy != null, BizCodes.NOT_FOUND.getCode(), "无法获取Deployment文件");
                logger.info("[setInfo] Get the deployment file: deploy={}",deploy);
                KubeCredentialWithTokenV1 kubeCredentialWithTokenV1 = this.applicationFeign.getUrlAndTokenByAppEnvId(pipeline.getAppEnvId());
                BizAssert.found(kubeCredentialWithTokenV1 != null, BizCodes.NOT_FOUND.getCode(), "无法获取集群信息");
                ip = kubeCredentialWithTokenV1.getTargetClusterUrl();
                token = kubeCredentialWithTokenV1.getTargetClusterToken();
                logger.info("[setInfo] Get the cluster info: ip={}, token={}",ip,token);

            }

            List<Stage> stages = pipeline.getStages();
            for (int i = 0; i < stages.size(); i++) {
                List<Step> steps = stages.get(i).getSteps();
                for (int j = 0; j < steps.size(); j++) {
                    Step task = steps.get(j);
                    StepType taskName = task.getTaskName();
                    switch (taskName) {
                        case PullCode:
                            task.setGitUrl(gitUrl == null ? task.getGitUrl() : gitUrl);
                            logger.info("[setInfo] Stage PullCode set");
                            break;
                        case BuildDocker:
                            task.setDockerUserName(dockerUserName == null ? task.getDockerUserName() : dockerUserName);
                            task.setRepository(repository == null ? task.getRepository() : repository);
                            task.setRepositoryVersion(repositoryVersion == null ? task.getRepositoryVersion() : repositoryVersion);
                            logger.info("[setInfo] Stage BuildDocker set");
                            break;
                        case PushDocker:
                            task.setDockerUserName(dockerUserName == null ? task.getDockerUserName() : dockerUserName);
                            task.setRepository(repository == null ? task.getRepository() : repository);
                            task.setRepositoryVersion(repositoryVersion == null ? task.getRepositoryVersion() : repositoryVersion);
                            task.setDockerPassword(dockerPassword == null ? task.getDockerPassword() : dockerPassword);
                            logger.info("[setInfo] Stage PushDocker set");
                            break;
                        case Deploy:
                            task.setDeploy(deploy);
                            task.setIp(ip);
                            task.setToken(token);
                            logger.info("[setInfo] Stage Deploy set");
                            break;
                    }
                }
            }
        }
        return pipeline;
    }


}
