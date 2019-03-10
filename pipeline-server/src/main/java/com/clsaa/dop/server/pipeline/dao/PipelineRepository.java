package com.clsaa.dop.server.pipeline.dao;

import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PipelineRepository extends MongoRepository<Pipeline, ObjectId>{

}
