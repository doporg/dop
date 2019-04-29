package com.clsaa.dop.server.pipeline.model.vo;

import com.clsaa.dop.server.pipeline.model.po.Jenkinsfile;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineVo {
    private String id;

    /**
     * 流水线名称
     */
    private String name;

    /**
     * 监听方式
     */
    private Pipeline.Monitor monitor;

    /**
     * 定时触发时间间隔
     */
    private Long timing;

    /**
     *  配置方式
     * */
    private Pipeline.Config config;

    /**
     *  Jenkinsfile
     * */
    private Jenkinsfile jenkinsfile;

    /**
     * 流水线阶段
     */
    private ArrayList<Stage> stages;

    /**
     * 流水线所属项目的id
     */
    private Long appId;

    /**
     * 流水线所属环境的id
     */
    private Long appEnvId;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    /**
     * 修改人
     */
    private Long cuser;
}
