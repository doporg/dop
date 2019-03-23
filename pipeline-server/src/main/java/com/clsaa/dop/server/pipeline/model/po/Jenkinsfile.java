package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.ArrayList;

/**
 *
 * 保存用户Jenkinsfile目录
 * @author 张富利
 * @since 2019-03-22
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jenkinsfile {

    /**
     * github地址
     */
    @SerializedName("git")
    private String git;

    /**
     * Jenkinsfile路径
     */
    @SerializedName("path")
    private String path;
}
