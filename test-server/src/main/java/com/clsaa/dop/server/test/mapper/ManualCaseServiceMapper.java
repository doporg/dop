package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class ManualCaseServiceMapper extends AbstractCommonServiceMapper<ManualCase, ManualCaseDto> {

    @Override
    Class<ManualCase> getSourceClass() {
        return ManualCase.class;
    }

    @Override
    Class<ManualCaseDto> getTargetClass() {
        return ManualCaseDto.class;
    }
}
