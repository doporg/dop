package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class InterfaceCaseDtoMapper extends AbstractCommonServiceMapper<InterfaceCase, InterfaceCaseDto> {

    @Autowired
    private InterfaceStageDtoMapper interfaceStageDtoMapper;

    @Override
    public Class<InterfaceCase> getSourceClass() {
        return InterfaceCase.class;
    }

    @Override
    public Class<InterfaceCaseDto> getTargetClass() {
        return InterfaceCaseDto.class;
    }

    @Override
    public Optional<InterfaceCaseDto> convert(InterfaceCase source) {
        return super.convert(source).map(fillStages(source));
    }

    private Function<InterfaceCaseDto, InterfaceCaseDto> fillStages(InterfaceCase interfaceCase) {
        return dto -> {
            List<InterfaceStageDto> interfaceStageDtos = interfaceStageDtoMapper.convert(interfaceCase.getStages());
            dto.setStages(interfaceStageDtos);
            return dto;
        };
    }
}

