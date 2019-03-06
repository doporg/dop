package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    // 任务id
    @SerializedName("id")
    private Long id;

    @SerializedName("taskName")
    private String taskName;

    @SerializedName("gitUrl")
    private String gitUrl;

    @SerializedName("dockerUserName")
    private String dockerUserName;

    @SerializedName("dockerPassword")
    private String dockerPassword;

    @SerializedName("repositoryVersion")
    private String repositoryVersion;

    @SerializedName("repository")
    private String repository;

    @SerializedName("description")
    private String description;
}
