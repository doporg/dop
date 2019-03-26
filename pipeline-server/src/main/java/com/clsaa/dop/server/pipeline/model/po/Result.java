package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


/**
 * 运行结果
 */


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @SerializedName("buildTime")
    private LocalDateTime buildTime;

    @SerializedName("output")
    private String output;
}
