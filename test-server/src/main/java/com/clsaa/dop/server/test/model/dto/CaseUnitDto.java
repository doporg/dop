package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.CaseType;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
public class CaseUnitDto {

    // 类型
    private CaseType caseType;

    private Long caseId;

    private String caseName;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
