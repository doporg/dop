package com.clsaa.dop.server.application.model.bo;

import lombok.*;

import javax.persistence.Column;


/**
 * 应用Url信息业务层对象
 *
 * @author Bowen
 * @since 2019-3-10
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUrlInfoBoV1 {


    private String warehouseUrl;

    private String productionDbUrl;

    private String testDbUrl;


    private String productionDomain;


    private String testDomain;
}
