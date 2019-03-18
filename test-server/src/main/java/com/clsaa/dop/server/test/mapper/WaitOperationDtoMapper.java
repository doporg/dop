package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.WaitOperationDto;
import com.clsaa.dop.server.test.model.po.WaitOperation;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class WaitOperationDtoMapper extends AbstractCommonServiceMapper<WaitOperation, WaitOperationDto> {

    @Override
    Class<WaitOperation> getSourceClass() {
        return WaitOperation.class;
    }

    @Override
    Class<WaitOperationDto> getTargetClass() {
        return WaitOperationDto.class;
    }
}
