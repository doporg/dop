package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceCaseDto {

    private Long id;

    private String caseName;

    private String caseDesc;

    private String preCondition;

    private Long applicationId;

    private String steps;

    private String predicateResult;

    private CaseStatus status;

    private List<InterfaceStageDto> stages;

    // ----------- common property ---------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

}
