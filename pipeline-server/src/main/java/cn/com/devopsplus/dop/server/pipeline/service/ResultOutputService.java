package cn.com.devopsplus.dop.server.pipeline.service;


import cn.com.devopsplus.dop.server.pipeline.dao.ResultOutputRepository;
import cn.com.devopsplus.dop.server.pipeline.feign.ApplicationFeign;
import cn.com.devopsplus.dop.server.pipeline.model.dto.LogInfoV1;
import cn.com.devopsplus.dop.server.pipeline.model.po.Pipeline;
import cn.com.devopsplus.dop.server.pipeline.model.po.ResultOutput;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 运行时，创建一条运行时的快照, 返回此快照的runningid
     */
    public String create(String pipelineid) {
        logger.info("[create] Request coming: pipelineid={}",pipelineid);
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
        logger.info("[setResult] Request coming: loginUser={}, pipelineId={}, output={}",loginUser,pipelineId,output);
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

        Pipeline pipeline = this.pipelineService.findById(pipelineId);

        String git = null;
        Pattern patternGit = Pattern.compile("http(s)*://\\S+.git");
        Matcher matcherGit = patternGit.matcher(output);
        if (matcherGit.find()) {
            git = matcherGit.group();
            git = git.split("\\.git")[0];
        }

        String commitId = null;
//        Pattern patternCommitId = Pattern.compile("git\\s+checkout\\s+-f\\s+[a-z0-9]+");
        Pattern patternCommitId = Pattern.compile("commitId\\s+\\S+");
        Matcher matcherCommitId = patternCommitId.matcher(output);
        if (matcherCommitId.find()) {
            commitId = matcherCommitId.group().split("\\s+")[1];
        }

        String repository= null;
        Pattern patternRepository = Pattern.compile("Successfully\\s+tagged(.*)+");
        Matcher matcherRepository = patternRepository.matcher(output);
        if (matcherRepository.find()) {
            repository = matcherRepository.group();
            repository = repository.split("\\s+")[2];
        }


        String status = this.jenkinsService.getBuildResult(pipelineId);
        LogInfoV1 logInfoV1 = LogInfoV1.builder()
                .runningId(running.getId().toString())
                .commitUrl(git + "/" + commitId)
                .imageUrl(repository)
                .rtime(running.getCtime())
                .ruser(loginUser)
                .Status(status)
                .build();
        this.applicationFeign.addLog(loginUser, pipeline.getAppEnvId(), logInfoV1);
    }

    public ResultOutput findByRunningId(String runningId) {
        logger.info("[findByRunningId] Request coming: runningId={}",runningId);
        Optional<ResultOutput> optionalResultOutput = this.resultOutputRepository.findById(new ObjectId(runningId));
        if (optionalResultOutput.isPresent()) {
            ResultOutput resultOutput = optionalResultOutput.get();
            return resultOutput;
        } else {
            return null;
        }
    }
}
