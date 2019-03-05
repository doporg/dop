package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.stereotype.Component;

@Component
public interface ManualCaseServiceMapper<ManualCase,ManualCaseDto> implements ServiceMapper<ManualCase, ManualCaseDto> {

    @Override
    public ManualCase downgrade(ManualCaseDto manualCaseDto) {
        return null;
    }

    @Override
    public ManualCaseDto upgrade(ManualCase manualCase) {
        return null;
    }
}
