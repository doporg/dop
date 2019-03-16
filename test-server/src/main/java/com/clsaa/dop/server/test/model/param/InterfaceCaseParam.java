package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import lombok.Data;

/**
 * @author xihao
 * @version 1.0
 * @since 16/03/2019
 */
@Data
public class InterfaceCaseParam {

    private String caseName;

    private String caseDesc;

    private String preCondition;

    private Long applicationId;

    private String steps;

    private String predicateResult;

    private CaseStatus status;

}
