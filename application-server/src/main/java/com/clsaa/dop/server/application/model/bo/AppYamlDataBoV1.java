package com.clsaa.dop.server.application.model.bo;

import com.clsaa.dop.server.application.model.po.AppYamlData;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppYamlDataBoV1 {

    private Long id;

    /**
     * 命名空间
     */
    private String nameSpace;

    /**
     * 对应服务
     */
    private String service;


    /**
     * 发布策略
     */
    private AppYamlData.ReleaseStrategy releaseStrategy;

    /**
     * 镜像地址
     */
    private String image_url;


}


