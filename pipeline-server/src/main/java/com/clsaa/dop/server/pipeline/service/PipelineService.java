package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.PipelineRepository;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.vo.PipelineV1;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 流水线业务实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */
@Service
public class PipelineService {

    @Autowired
    private PipelineRepository pipelineRepository;

    public void addPipeline(PipelineV1 pipelineV1) {
        Pipeline pipeline = Pipeline.builder()
                .id(new ObjectId())
                .name(pipelineV1.getName())
                .monitor(pipelineV1.getMonitor())
                .stages(pipelineV1.getStages())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(pipelineV1.getCuser()).build();

        System.out.println(pipeline.getId());
        pipelineRepository.insert(pipeline);
    }
}
