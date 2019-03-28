package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Result;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import org.apache.commons.collections.map.HashedMap;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class ResultOutputService {
    @Autowired
    private ResultOutputRepository resultOutputRepository;

    /**
     * 创建流水线时，同时创建一个该流水线运行的结果
     */
    public void create(Pipeline pipeline) {
        Result result = new Result(pipeline.getCtime(), "新建流水线");
        ArrayList<Result> results = new ArrayList<>();
        results.add(result);
        ResultOutput resultOutput = ResultOutput.builder()
                .id(pipeline.getId())
                .results(results)
                .ctime(pipeline.getCtime())
                .mtime(pipeline.getMtime())
                .cuser(pipeline.getCuser())
                .isDeleted(false)
                .build();
        this.resultOutputRepository.insert(resultOutput);
    }

    public void setResult(String id, String output) {
        Optional<ResultOutput> optionalResultOutput = this.resultOutputRepository.findById(new ObjectId(id));
        if (optionalResultOutput.isPresent()) {
            ResultOutput resultOutput = optionalResultOutput.get();
            ArrayList<Result> results = resultOutput.getResults();
            Result result = new Result(LocalDateTime.now(), output);
            results.add(result);
            resultOutput.setResults(results);
            resultOutputRepository.save(resultOutput);
        }
    }

    public void delete(String id) {
        Optional<ResultOutput> optionalResultOutput = this.resultOutputRepository.findById(new ObjectId(id));
        if (optionalResultOutput.isPresent()) {
            ResultOutput resultOutput = optionalResultOutput.get();
            resultOutput.setIsDeleted(true);
            resultOutputRepository.save(resultOutput);
        }
    }
}
