package com.clsaa.dop.server.pipeline.util.step;

import com.clsaa.dop.server.pipeline.config.BizCodes;
import com.clsaa.dop.server.pipeline.config.ImageRepositoryConfig;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class StepGenerationOption {

    private final String directory;
    private final Map<String, String> valuesMap;

    public StepGenerationOption(String directory, Map<String, String> valuesMap) {
        this.directory = directory;
        this.valuesMap = valuesMap;
    }

    public static StepGenerationOption fromStepPlaceholder(Step placeholder) {
        String directory = null;
        if(placeholder.getGitUrl() != null && !placeholder.getGitUrl().isEmpty()){
            String[] urlSplits = placeholder.getGitUrl().split("/");
            String lastSplit = urlSplits[urlSplits.length - 1];

            directory = lastSplit.split("[.]")[0];
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> valuesMap = mapper.convertValue(placeholder, new TypeReference<HashMap<String, String>>() {});
        if (placeholder.getRepository() != null) {
            String repository = placeholder.getRepository();
            String repositoryHost = ImageRepositoryConfig.repositoryHostPort;
            String repositoryPath = "/default";
            String imageName = repositoryHost + repositoryPath;
            try {
                if (repository.startsWith("http")) {
                    URL dockerRepoUrl = new URL(repository);
                    repositoryHost = dockerRepoUrl.getHost();
                    repositoryPath = dockerRepoUrl.getPath();
                    imageName = repositoryHost + repositoryPath;
                } else {
                    repositoryHost = repository.split("/")[0];
                    imageName = repository;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                BizAssert.justFailed(new BizCode(BizCodes.INVALID_PARAM.getCode(), e.getMessage()));
            }
            valuesMap.put("dockerRepoHost", repositoryHost);
            valuesMap.put("imageName", imageName);
        }

        return new StepGenerationOption(directory, valuesMap);
    }

    public StepGenerationOption with(List<Pair<String, String>> pairs) {
        Map<String, String> newValueMap = new HashMap<>(valuesMap);
        for (Pair<String, String> pair: pairs)
            newValueMap.put(pair.getKey(), pair.getValue());

        return new StepGenerationOption(directory, newValueMap);
    }

    public boolean withDirectory() {
        return this.directory != null;
    }
    public StepGenerationOption withDirectory(String directory) {
        if (directory == null)
            return this;
        return new StepGenerationOption(directory, this.getValuesMap());
    }
}
