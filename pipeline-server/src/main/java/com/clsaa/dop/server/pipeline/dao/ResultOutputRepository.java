package com.clsaa.dop.server.pipeline.dao;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResultOutputRepository extends MongoRepository<ResultOutput, ObjectId> {
}
