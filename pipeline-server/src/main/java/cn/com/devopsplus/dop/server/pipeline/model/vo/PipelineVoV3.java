package cn.com.devopsplus.dop.server.pipeline.model.vo;

import lombok.*;

import java.time.LocalDateTime;

/***
 * 展示全部流水线信息---for table
 * **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineVoV3 {
    /**
     * 流水线id
     */
    private String id;

    /**
     * 流水线名称
     */
    private String name;

    /**
     * 修改人
     */
    private String cuser;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;
}
