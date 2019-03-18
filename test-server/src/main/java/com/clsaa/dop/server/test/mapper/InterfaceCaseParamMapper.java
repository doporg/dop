package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 16/03/2019
 */
@Component
public class InterfaceCaseParamMapper extends AbstractCommonServiceMapper<InterfaceCaseParam, InterfaceCaseDto> {

    @Override
    Class<InterfaceCaseParam> getSourceClass() {
        return InterfaceCaseParam.class;
    }

    @Override
    Class<InterfaceCaseDto> getTargetClass() {
        return InterfaceCaseDto.class;
    }


    public void fillDateAndUser(InterfaceCaseDto dto) {
        LocalDateTime current = LocalDateTime.now();
        dto.setCtime(current);
        dto.setMtime(current);

    }
}
