package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitOperationDto {

    private OperationType operationType;

    /**
     * 单位为毫秒
     */
    private int waitTime;

    private int order;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
