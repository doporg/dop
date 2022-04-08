package cn.com.devopsplus.dop.server.pipeline.model.vo;

import cn.com.devopsplus.dop.server.pipeline.model.po.Jenkinsfile;
import lombok.*;

import java.util.ArrayList;


/***
 * 返回流水线信息
 *
 * */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineVoV1 {
    private String id;

    private String name;

    private int monitor;

    private String timing;

    private int config;

    private Jenkinsfile jenkinsfile;

    private ArrayList<StageVoV1> stages;

    private Long appId;

    private Long appEnvId;

}
