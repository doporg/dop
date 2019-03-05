package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ManualCaseServiceMapper implements ServiceMapper<ManualCase, ManualCaseDto> {

    public static ManualCaseServiceMapper MAPPER = Mappers.getMapper(ManualCaseServiceMapper.class);

    @Override
    public abstract ManualCase downgrade(ManualCaseDto dto);

    @Override
    public abstract ManualCaseDto upgrade(ManualCase po);

}
