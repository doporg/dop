package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class InterfaceCaseMapper extends AbstractCommonServiceMapper<InterfaceCase, InterfaceCaseDto> {

    @Override
    Class<InterfaceCase> getPOClass() {
        return InterfaceCase.class;
    }

    @Override
    Class<InterfaceCaseDto> getDTOClass() {
        return InterfaceCaseDto.class;
    }

}

