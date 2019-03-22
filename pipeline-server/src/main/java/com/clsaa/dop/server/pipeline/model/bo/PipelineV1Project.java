package com.clsaa.dop.server.pipeline.model.bo;

import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 向应用管理模块返回数据
 * */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineV1Project {

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

}
