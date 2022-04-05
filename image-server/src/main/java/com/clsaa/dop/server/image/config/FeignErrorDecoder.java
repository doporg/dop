package com.clsaa.dop.server.image.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clsaa.rest.result.exception.*;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 讲FeignException转换为标准业务异常
 *
 * @author joyren
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Exception decode(String methodKey, Response response) {
        logger.info("feign error occur: method=" + methodKey + ", response=" + response.toString());
        
        try {
            // to prevent from NullPointerException when parse a null `response.body()`
            String body = "{code:\"500\", message:\"no content in this message body\"}";
            if (response.body() != null) {
                body = Util.toString(response.body().asReader());
            }
            JSONObject jsonObject = JSON.parseObject(body);
            int code = Integer.parseInt(String.valueOf(jsonObject.get("code")));
            String msg = String.valueOf(jsonObject.get("message"));
            switch (response.status()) {
                case 400:
                    return new InvalidParameterException(code, msg);
                case 403:
                    return new AccessDeniedException(code, msg);
                case 404:
                    return new NotFoundException(code, msg);
                case 417:
                    return new StandardBusinessException(code, msg);
                case 401:
                    return new UnauthorizedException(code, msg);
                default:
                    return new RuntimeException(body);
            }
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}