package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.feign.ApplicationFeign;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.dto.LogInfoV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ResultOutputService {

    @Autowired
    private ResultOutputRepository resultOutputRepository;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private JenkinsService jenkinsService;
    @Autowired
    private ApplicationFeign applicationFeign;

    /**
     * 运行时，创建一条运行时的快照, 返回此快照的runningid
     */
    public String create(String pipelineid) {
        ObjectId id = new ObjectId();
        ResultOutput resultOutput = ResultOutput.builder()
                .id(id)
                .pipelineId(pipelineid)
                .ctime(LocalDateTime.now())
                .result("")
                .status(ResultOutput.Status.RUNNING)
                .isDeleted(false).build();

        this.resultOutputRepository.insert(resultOutput);
        return id.toString();
    }

    public void setResult(String pipelineId, String output, Long loginUser) {
        List<ResultOutput> resultOutputs = this.resultOutputRepository.findByPipelineId(pipelineId);
        ResultOutput running = null;
        for (int i = 0; i < resultOutputs.size(); i++) {
            ResultOutput resultOutput = resultOutputs.get(i);
            if (resultOutput.getStatus() == ResultOutput.Status.RUNNING) {
                running = resultOutput;
            }
        }
        running.setStatus(ResultOutput.Status.FINISHED);
        running.setResult(output);
        this.resultOutputRepository.save(running);

        PipelineBoV1 pipelineBoV1 = this.pipelineService.findById(new ObjectId(pipelineId));
        String gitUrl = null;
        String repository = null;
        List<Stage> stages = pipelineBoV1.getStages();
        for (int i = 0; i < stages.size(); i++) {
            List<Step> steps = stages.get(i).getSteps();
            for (int j = 0; j < steps.size(); j++) {
                Step task = steps.get(j);
                String taskName = task.getTaskName();
                switch (taskName) {
                    case ("拉取代码"):
                        gitUrl = task.getGitUrl();
                        break;
                    case ("构建docker镜像"):
                    case ("推送docker镜像"):
                        repository = task.getRepository();
                        break;
                }
            }
            LogInfoV1 logInfoV1 = LogInfoV1.builder()
                    .runningId(running.getId().toString())
                    .commitUrl(gitUrl)
                    .imageUrl(repository)
                    .rtime(running.getCtime())
                    .ruser(pipelineBoV1.getCuser())
                    .Status(this.jenkinsService.getBuildResult(pipelineBoV1.getId()))
                    .build();
//            this.applicationFeign.addLog(loginUser, pipelineBoV1.getAppEnvId(), logInfoV1);
        }
    }

    public ResultOutput findByRunningId(String runningId) {
        Optional<ResultOutput> optionalResultOutput = this.resultOutputRepository.findById(new ObjectId(runningId));
        if (optionalResultOutput.isPresent()) {
            ResultOutput resultOutput = optionalResultOutput.get();
            return resultOutput;
        } else {
            return null;
        }
    }
}
