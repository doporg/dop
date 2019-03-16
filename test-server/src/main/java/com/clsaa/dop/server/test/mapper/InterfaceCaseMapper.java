package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class InterfaceCaseMapper extends AbstractCommonServiceMapper<InterfaceCase, InterfaceCaseDto> {

    @Override
    Class<InterfaceCase> getSourceClass() {
        return InterfaceCase.class;
    }

    @Override
    Class<InterfaceCaseDto> getTargetClass() {
        return InterfaceCaseDto.class;
    }

    @Override
    public Optional<InterfaceCaseDto> convert(InterfaceCase source) {
        return Optional.of(source).map(doConvert());
    }

    private Function<InterfaceCase, InterfaceCaseDto> doConvert() {
        return po -> {
            if (po != null) {
                InterfaceCaseDto dto = BeanUtils.convertType(po, InterfaceCaseDto.class);
                dto.setStages(po.getStages());
                return dto;
            }
            return null;
        };
    }
}

