package com.clsaa.dop.server.user.enums;

/**
 * 用户类型枚举
 *
 * @author 任贵杰
 */
public enum UserType {

    /**
     * 开发人员
     */
    PROGRAMMER("开发"),
    /**
     * 测试人员
     */
    TESTERS("测试"),
    ;

    private String type;

    private UserType(String type) {
    }
}