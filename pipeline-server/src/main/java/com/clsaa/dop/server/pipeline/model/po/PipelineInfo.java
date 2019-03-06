package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pipelineInfo")
public class PipelineInfo {
    @SerializedName("id")
    private Long Id;

    @Field("name")
    @SerializedName("name")
    private String name;

    @Field("creator")    //User id
    @SerializedName("creator")
    private String creator;

    @Field("admin")     //User id 的数组
    @SerializedName("admin")
    private ArrayList<String> admin;

    @Field("monitor")
    @SerializedName("monitor")
    private String monitor;

    @Field("createTime")
    @SerializedName("createTime")
    private String createTime;

    @Field("stage")
    @SerializedName("stage")
    private ArrayList<Stage> stage;
}
