package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.ParamClass;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Data
public class RequestParamCreateParam {

    private ParamClass paramClass;

    @NotNull
    private String name;

    @NotNull
    private String value;

}
