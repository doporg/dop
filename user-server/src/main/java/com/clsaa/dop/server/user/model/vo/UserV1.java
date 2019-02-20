package com.clsaa.dop.server.user.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * <p>
 * 用户视图层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Getter
@Setter
public class UserV1 {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户email地址
     */
    private String email;
    /**
     * 创建时间
     */
    private Timestamp ctime;
    /**
     * 修改时间
     */
    private Timestamp mtime;
    /**
     * 用户类型
     */
    private UserType type;
}