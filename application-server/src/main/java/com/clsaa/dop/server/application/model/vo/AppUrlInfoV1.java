package com.clsaa.dop.server.application.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUrlInfoV1 {

    private String warehouseUrl;

    private String productionDbUrl;

    private String testDbUrl;

    private String productionDomain;

    private String testDomain;
}
