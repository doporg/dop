package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * 流水线信息持久层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dop_pipeline_server")
public class Pipeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("id")
    private String id;

    /**
     * 流水线名称
     */
    @Field("name")
    @SerializedName("name")
    private String name;

    /**
     * 监听方式
     */
    @Field("monitor")
    @SerializedName("monitor")
    @Enumerated(EnumType.STRING)
    private Monitor monitor;
    public enum Monitor {AutomaticTrigger, ManualTrigger, TimingTrigger;}

    /**
     * 定时触发时间间隔
     */
    @Field("timing")
    @SerializedName("timing")
    private Long timing;

    /**
     *  配置方式
     * */
    @Field("config")
    @SerializedName("config")
    private Config config;
    public enum Config {HasJenkinsfile, NoJenkinsfile;}

    /**
     *  Jenkinsfile
     * */
    @Field("jenkinsfile")
    @SerializedName("jenkinsfile")
    private Jenkinsfile jenkinsfile;

    /**
     * 流水线阶段
     */
    @Field("stages")
    @SerializedName("stages")
    private ArrayList<Stage> stages;

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
     * 修改人
     */
    @Field("cuser")
    @SerializedName("cuser")
    private Long cuser;

    /**
     * 是否删除
     */
    @Field("isDeleted")
    @SerializedName("isDeleted")
    private Boolean isDeleted;

}
