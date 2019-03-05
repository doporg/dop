package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InterfaceCaseDto {

    public InterfaceCaseDto(Long id, LocalDateTime ctime, LocalDateTime mtime, Long cuser,
                            Long muser, boolean deleted, String caseName, String caseDesc,
                            String preCondition, Long applicationId, String steps, String predicateResult,
                            CaseStatus status) {
        this.id = id;
        this.ctime = ctime;
        this.mtime = mtime;
        this.cuser = cuser;
        this.muser = muser;
        this.deleted = deleted;
        this.caseName = caseName;
        this.caseDesc = caseDesc;
        this.preCondition = preCondition;
        this.applicationId = applicationId;
        this.steps = steps;
        this.predicateResult = predicateResult;
        this.status = status;
    }

    public InterfaceCaseDto() {
    }

    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    private String caseName;

    private String caseDesc;

    private String preCondition;

    private Long applicationId;

    private String steps;

    private String predicateResult;

    private CaseStatus status;
}
