package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import lombok.Data;

import javax.validation.Valid;
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

    private List<@Valid RequestHeaderParam> requestHeaders;

    private List<@Valid RequestParamCreateParam> requestParams;

    private String requestBody;

    private List<@Valid RequestCheckPointParam> requestCheckPoints;

//    @Min(1)
    private int retryTimes;

//    @Min(1)
    private Long retryInterval;

    private List<@Valid RequestResultParam> resultParams;

    @NotNull
    private OperationType operationType;

    private int order;
}
