package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.PipelineRepository;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.vo.PipelineVoV1;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    /**
     * 添加流水线信息
     */
    public void addPipeline(PipelineVoV1 pipelineV1) {
        Pipeline pipeline = Pipeline.builder()
                .id(new ObjectId())
                .name(pipelineV1.getName())
                .monitor(pipelineV1.getMonitor())
                .stages(pipelineV1.getStages())
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(pipelineV1.getCuser())
                .isDeleted(false)
                .build();

        pipelineRepository.insert(pipeline);
    }

    /**
     * 获得全部流水线信息, 存在返回全部流水线信息，若不存在返回null
     */
    public List<PipelineBoV1> findAll() {
        List<Pipeline> pipelines = this.pipelineRepository.findAll();
        List<PipelineBoV1> pipelineBoV1s = new ArrayList<PipelineBoV1>();
        for(int i = 0; i < pipelines.size(); i++){
            PipelineBoV1 pipelineBoV1 = PipelineBoV1.builder()
                    .id(pipelines.get(i).getId().toString())
                    .name(pipelines.get(i).getName())
                    .monitor(pipelines.get(i).getMonitor())
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
        if(optionalPipeline.isPresent()){
            Pipeline pipeline = optionalPipeline.get();
            PipelineBoV1 pipelineBoV1 = PipelineBoV1.builder()
                    .id(pipeline.getId().toString())
                    .name(pipeline.getName())
                    .monitor(pipeline.getMonitor())
                    .stages(pipeline.getStages())
                    .ctime(pipeline.getCtime())
                    .mtime(pipeline.getMtime())
                    .cuser(pipeline.getCuser())
                    .isDeleted(pipeline.getIsDeleted())
                    .build();
            return pipelineBoV1;
        }else{
            return null;
        }
    }
}
