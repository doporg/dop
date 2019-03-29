package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
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

    public void setResult(String pipelineId, String output) {
        List<ResultOutput> resultOutputs = this.resultOutputRepository.findByPipelineId(pipelineId);
        ResultOutput running = null;
        for (int i = 0; i < resultOutputs.size(); i++) {
            ResultOutput resultOutput = resultOutputs.get(i);
            if(resultOutput.getStatus() == ResultOutput.Status.RUNNING){
                running = resultOutput;
            }
        }
        running.setStatus(ResultOutput.Status.FINISHED);
        running.setResult(output);
        this.resultOutputRepository.save(running);
    }

}
