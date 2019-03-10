package com.clsaa.dop.server.application.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class AppBasicInfoV1 {

    /**
     * 应用拥有者
     */
    private Long ouser;

    /**
     * 应用名称
     */
    private String title;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 应用描述
     */
    private String description;

    private String warehouseUrl;

    private String productionDbUrl;

    private String testDbUrl;

    private String productionDomain;

    private String testDomain;
}
