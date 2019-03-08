package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private InterfaceCase interfaceCase;



}
