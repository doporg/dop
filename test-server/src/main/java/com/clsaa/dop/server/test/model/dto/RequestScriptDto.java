package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.doExecute.matcher.ToStringMatcher;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.model.context.ExecuteContext;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.output.WriterOutputStream;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.StringWriter;
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
    public void run(ExecuteContext executeContext) {
        try {
            OperationExecuteLog operationExecuteLog = initOperationLog(executeContext);
            Map<String, String> headers = requestHeaders.stream()
                    .collect(Collectors.toMap(RequestHeaderDto::getName, RequestHeaderDto::getValue));
            StringBuilder executionLog = executeContext.logRequestInfo(headers, requestBody, httpMethod, rawUrl);

            //string writer to store response info
            StringWriter writer = new StringWriter();
            PrintStream captor = new PrintStream(new WriterOutputStream(writer), true);
            RestAssuredConfig config = RestAssured.config()
                    .logConfig(new LogConfig(captor, true));

            RequestSpecification requestSpecification = RestAssured.given()
                    .config(config)
                    .headers(headers)
                    .body(requestBody);

            URL url = new URL(rawUrl);
            //http call and log response
            ValidatableResponse response = httpCall(requestSpecification, url, httpMethod)
                                            .then()
                                            .log().everything(true);
            executionLog = executeContext.logResponseInfo(executionLog, writer);

            // auto test
            for (RequestCheckPointDto checkPoint : requestCheckPoints) {
                Matcher matcher = getMatcher(checkPoint);
                try {
                    response.body(checkPoint.getProperty(), matcher);
                } catch (AssertionError error) {
                    setResultFail();
                    checkPoint.fail(error.getMessage());
                }
            }

            // collect log info
            operationExecuteLog.setExecuteInfo(executionLog.toString());
            executeContext.addOperationLog(endOperationLog(operationExecuteLog));
        } catch (Exception e) {
            log.error(e.getMessage());
            setResultFail();
        }
    }

    private void setResultFail() {
        if (!FAIL_RESULT.equals(this.result)) {
            this.result = FAIL_RESULT;
        }
    }

    private void setResultSuccess() {
        this.result = SUCCESS_RESULT;
    }

    private Matcher getMatcher(RequestCheckPointDto checkPoint) {
        switch (checkPoint.getOperation()) {
            case EQUALS:
                return ToStringMatcher.hasToString(checkPoint.getValue());
            case NOTEQUALS:
                return ToStringMatcher.notToString(checkPoint.getValue());
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
