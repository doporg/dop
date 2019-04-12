package com.clsaa.dop.server.test.doExecute.context;

import com.clsaa.dop.server.test.doExecute.Version;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

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

    private Map<String, String> params;
}
