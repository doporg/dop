package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestScriptDto {

    // ----------- main property ---------
    private String rawUrl;

    private HttpMethod httpMethod;

    private List<RequestHeaderDto> requestHeaders;

    private String requestBody;

    // check point concerned
    private List<RequestCheckPointDto> requestCheckPoints;

    private int retryTimes;

    private Long retryInterval;

    private List<UrlResultParamDto> resultParams;

    private OperationType operationType;

    private int order;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
