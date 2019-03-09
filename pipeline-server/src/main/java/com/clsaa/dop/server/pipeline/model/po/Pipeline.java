package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
@Document(collection = "pipeline")
public class Pipeline {
    @Id
    @SerializedName("id")
    private Long id;

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
    private String monitor;

    /**
     * 流水线阶段
     */
    @Field("stages")
    @SerializedName("stages")
    private ArrayList<Stage> stages;

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
