package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class InterfaceCaseMapper implements ServiceMapper<InterfaceCase, InterfaceCaseDto> {

    public static InterfaceCaseMapper MAPPER = Mappers.getMapper(InterfaceCaseMapper.class);

    @Override
    @Mappings(value = {

    })
    public abstract InterfaceCase downgrade(InterfaceCaseDto interfaceCaseDto);

    @Override
    @Mappings(value = {

    })
    public abstract InterfaceCaseDto upgrade(InterfaceCase interfaceCase);

}

