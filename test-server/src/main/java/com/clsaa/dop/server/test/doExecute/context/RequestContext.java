package com.clsaa.dop.server.test.doExecute.context;

import com.clsaa.dop.server.test.doExecute.Version;
import com.clsaa.dop.server.test.enums.ParamClass;
import com.clsaa.dop.server.test.model.dto.RequestParamDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
@Data
@Builder
public class RequestContext {

    public Version version;

    private String url;

    private Map<String, String> requestHeaders;

    private String requestBody;

    private Map<String, String> caseParams;

    private Map<ParamClass, List<RequestParamDto>> requestParams;

    public void fillRequestParams(List<RequestParamDto> requestParamDtos) {
        if (CollectionUtils.isEmpty(requestParamDtos)) {
            this.requestParams = new HashMap<>();
            return;
        }
        this.requestParams = requestParamDtos.stream()
                .collect(Collectors.groupingBy(RequestParamDto::getParamClass));
    }
}
