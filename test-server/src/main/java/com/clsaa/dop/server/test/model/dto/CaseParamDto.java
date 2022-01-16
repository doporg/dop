package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 11/04/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaseParamDto {

    private String ref;

    private String value;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
