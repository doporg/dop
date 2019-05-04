package com.clsaa.dop.server.test.enums;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
public enum  ParamClass {

    GET_PARAM("get param"),
    PATH_PARAM("path param"),
    FILE_PARAM("file param")
    ;

    private String comment;

    ParamClass(String comment) {
        this.comment = comment;
    }
}
