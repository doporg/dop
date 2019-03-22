package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.clsaa.dop.server.test.doExecute.TestManager.FAIL_RESULT;
import static com.clsaa.dop.server.test.doExecute.TestManager.SUCCESS_RESULT;
import static com.clsaa.dop.server.test.enums.OperationType.REQUEST;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestScriptDto implements Operation {

    private static final Logger log = LoggerFactory.getLogger(RequestScriptDto.class);

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

    private String result = SUCCESS_RESULT;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    @Override
    public void run() {
        try {
            Map<String, String> headers = requestHeaders.stream()
                    .collect(Collectors.toMap(RequestHeaderDto::getName, RequestHeaderDto::getValue));
            RequestSpecification requestSpecification = RestAssured.given().headers(headers).body(requestBody);
            URL url = new URL(rawUrl);
            ValidatableResponse response = httpCall(requestSpecification, url, httpMethod).then();
            for (RequestCheckPointDto checkPoint : requestCheckPoints) {
                Matcher matcher = getMatcher(checkPoint);
                try {
                    response.body(checkPoint.getProperty(), matcher);
                } catch (AssertionError error) {
                    setResultFail();
                    checkPoint.fail(error.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            setResultFail();
        }
    }

    private void setResultFail() {
        this.result = FAIL_RESULT;
    }

    private void setResultSuccess() {
        this.result = SUCCESS_RESULT;
    }

    private Matcher getMatcher(RequestCheckPointDto checkPoint) {
        switch (checkPoint.getOperation()) {
            case EQUALS:
                return Matchers.equalTo(checkPoint.getValue());
            case NOTEQUALS:
                return Matchers.not(checkPoint.getValue());
            default:
                throw new UnsupportedOperationException(String.format("不支持的校验操作: %s", checkPoint.getOperation()));
        }
    }

    private Response httpCall(RequestSpecification requestSpecification, URL url, HttpMethod httpMethod) {
        Response response;
        switch (httpMethod) {
            case GET:
                response = requestSpecification.get(url);
                break;
            case POST:
                response = requestSpecification.post(url);
                break;
            case PUT:
                response = requestSpecification.put(url);
                break;
            case DELETE:
                response = requestSpecification.delete(url);
                break;
            default:
                throw new UnsupportedOperationException(String.format("不支持的请求方法: %s", httpMethod.toString()));
        }
        return response;
    }

    @Override
    public OperationType type() {
        return REQUEST;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public String result() {
        return result;
    }
}
