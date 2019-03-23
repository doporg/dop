package com.clsaa.dop.server.pipeline.model.bo;

import com.clsaa.dop.server.pipeline.model.po.Jenkinsfile;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * 流水线信息业务层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineBoV1 {
    /**
     * 流水线id
     */
    @SerializedName("id")
    private String id;

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
     * 流水线阶段
     */
    @SerializedName("stages")
    private ArrayList<Stage> stages;

    /**
     * 修改人
     */
    @SerializedName("cuser")
    private Long cuser;

    /**
     * 创建时间
     */
    @Field("ctime")
    @SerializedName("ctime")
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    @Field("mtime")
    @SerializedName("mtime")
    private LocalDateTime mtime;

    /**
     * 流水线所属项目的id
     */
    @Field("appId")
    @SerializedName("appId")
    private Long appId;

    /**
     * 流水线所属环境的id
     */
    @Field("appEnvId")
    @SerializedName("appEnvId")
    private Long appEnvId;


    /**
     * 是否删除
     */
    @Field("isDeleted")
    @SerializedName("isDeleted")
    private Boolean isDeleted;
}
