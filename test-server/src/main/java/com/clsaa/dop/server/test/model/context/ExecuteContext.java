package com.clsaa.dop.server.test.model.context;

import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.Stage;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import com.clsaa.dop.server.test.util.ExecutionLogUtils;
import lombok.Builder;
import lombok.Data;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
@Data
@Builder
public class ExecuteContext {

    private InterfaceExecuteLog interfaceExecuteLog;

    private InterfaceCaseDto interfaceCaseDto;

    private Stage currentStage;

    public void logAfterExecution() {
        interfaceExecuteLog.setEnd(LocalDateTime.now());
        interfaceExecuteLog.setSuccess(interfaceCaseDto.isExecuteSuccess());
        // persist log
        ExecutionLogUtils.saveLog(interfaceExecuteLog);
    }

    public void addOperationLog(OperationExecuteLog operationExecuteLog) {
        if (nonNull(interfaceExecuteLog)
                && nonNull(operationExecuteLog)
                && nonNull(interfaceExecuteLog.getOperationExecuteLogs())) {
            interfaceExecuteLog.getOperationExecuteLogs().add(operationExecuteLog);
        }
    }

    public ExecuteContext url(StringBuilder origin, HttpMethod method, String url) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append(method)
                .append(" ")
                .append(url)
                .append(nextLine);
        return this;
    }

    public ExecuteContext headers(StringBuilder origin, Map<String, String> headers) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append("Request Headers:")
                .append(nextLine);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String name = header.getKey();
            String value = header.getValue();
            origin.append(' ').append(name).append(" : ").append(value).append(' ').append(nextLine);
        }
        return this;
    }

    public ExecuteContext body(StringBuilder origin, String body) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append("Request Body:")
                .append(nextLine)
                .append(body);
        return this;
    }

    public StringBuilder get(StringBuilder s) {
        return s;
    }

    public StringBuilder logRequestInfo(Map<String, String> headers, String body, HttpMethod method, String url) {
        StringBuilder origin = new StringBuilder();
        return url(origin, method, url).headers(origin, headers).body(origin, body).get(origin);
    }

    public StringBuilder logResponseInfo(StringBuilder origin, StringWriter writer) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append(nextLine)
                .append("Response: ")
                .append(nextLine)
                .append(writer.toString().trim());
        return origin;
    }
}
