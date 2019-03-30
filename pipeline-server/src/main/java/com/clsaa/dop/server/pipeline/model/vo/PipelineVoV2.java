package com.clsaa.dop.server.pipeline.model.vo;

import com.clsaa.dop.server.pipeline.model.po.Jenkinsfile;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineVoV2 {

    /**
     * 流水线名称
     */
    @SerializedName("name")
    private String name;

    /**
     * 监听方式
     */
    @SerializedName("monitor")
    private String monitor;

    /**
     *  配置方式
     * */
    @SerializedName("config")
    private String config;

    /**
     *  Jenkinsfile
     * */
    @SerializedName("jenkinsfile")
    private Jenkinsfile jenkinsfile;

    /**
     * 流水线所属项目的id
     */
    @Field("appId")
    @SerializedName("appId")
    private Long appId;


    /**
     * 流水线阶段
     */
    @SerializedName("stages")
    private ArrayList<Stage> stages;


    /**
     * 修改人
     */
    @SerializedName("cuser")
    private Long cuser;
}
