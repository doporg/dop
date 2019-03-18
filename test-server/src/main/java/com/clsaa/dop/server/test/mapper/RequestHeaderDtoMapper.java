package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.RequestHeaderDto;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestHeaderDtoMapper extends AbstractCommonServiceMapper<RequestHeader, RequestHeaderDto> {

    @Override
    Class<RequestHeader> getSourceClass() {
        return RequestHeader.class;
    }

    @Override
    Class<RequestHeaderDto> getTargetClass() {
        return RequestHeaderDto.class;
    }
}
