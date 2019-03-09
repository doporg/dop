package com.clsaa.dop.server.pipeline.dao;

import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface PipelineRepository extends MongoRepository<Pipeline, Long>{

}
