package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

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

    @Override
    public Optional<InterfaceCaseDto> convert(InterfaceCaseParam param) {
        return super.convert(param).map(fillDateAndUser());
    }

    public Function<InterfaceCaseDto,InterfaceCaseDto> fillDateAndUser() {
        return dto -> {
            LocalDateTime current = LocalDateTime.now();
            dto.setCtime(current);
            dto.setMtime(current);

            //todo get current user
            dto.setCuser(110L);
            dto.setMuser(110L);
            return dto;
        };
    }
}
