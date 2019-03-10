package com.clsaa.dop.server.pipeline.model.po;


import com.google.gson.annotations.SerializedName;
import lombok.*;
/**
 * 流水线阶段所做任务持久层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Task {
    @SerializedName("id")
    private Long id;

    /**
     * 任务名称
     */
    @SerializedName("taskName")
    private String taskName;

    /**
     * git地址
     */
    @SerializedName("gitUrl")
    private String gitUrl;

    /**
     * docker用户名
     */
    @SerializedName("dockerUserName")
    private String dockerUserName;

    /**
     * docker密码
     */
    @SerializedName("dockerPassword")
    private String dockerPassword;

    /**
     * docker镜像版本
     */
    @SerializedName("repositoryVersion")
    private String repositoryVersion;

    /**
     * docker镜像名称
     */
    @SerializedName("repository")
    private String repository;

    /**
     * shell
     */
    @SerializedName("shell")
    private String shell;
}
