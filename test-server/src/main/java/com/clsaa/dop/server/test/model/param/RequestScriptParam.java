package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.model.dto.RequestCheckPointDto;
import com.clsaa.dop.server.test.model.dto.RequestHeaderDto;
import com.clsaa.dop.server.test.model.dto.UrlResultParamDto;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
public class RequestScriptParam {

    @NotNull
    private String rawUrl;

    @NotNull
    private HttpMethod httpMethod;

    private List<RequestHeaderParam> requestHeaders;

    private String requestBody;

    // check point concerned
    private List<RequestCheckPointParam> requestCheckPoints;

    @Min(1)
    private int retryTimes;

    @Min(1)
    private Long retryInterval;

    private List<RequestResultParam> resultParams;

    @NotNull
    private OperationType operationType;

//    @Min(0)
    private int order;
}
