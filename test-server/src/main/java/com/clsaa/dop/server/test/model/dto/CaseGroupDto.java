package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.ExecuteWay;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
public class CaseGroupDto {

    private Long appId;

    private ExecuteWay executeWay;

    private String groupName;

    private String comment;

    private List<CaseUnitDto> caseUnits;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
