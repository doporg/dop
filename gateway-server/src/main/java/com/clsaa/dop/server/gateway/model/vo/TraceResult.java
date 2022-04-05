package com.clsaa.dop.server.gateway.model.vo;

import com.clsaa.rest.result.bizassert.BizCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraceResult {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String path;
    private Integer code;
    private String message;
    private String error = "";
    private String trace = "";

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final TraceResult target;

        public Builder() {
            target = new TraceResult();
            target.timestamp = LocalDateTime.now();
        }

        public Builder path(String path) {
            target.path = path;
            return this;
        }

        public Builder bizCode(BizCode bizCode) {
            target.code = bizCode.getCode();
            target.message = bizCode.getMessage();
            return this;
        }

        public Builder error(String error) {
            target.error = error;
            return this;
        }

        public Builder trace(String trace) {
            target.trace = trace;
            return this;
        }

        public TraceResult build() {
            return target;
        }

    }

}
