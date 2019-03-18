package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.RequestCheckPointDto;
import com.clsaa.dop.server.test.model.po.RequestCheckPoint;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestCheckPointDtoMapper extends AbstractCommonServiceMapper<RequestCheckPoint, RequestCheckPointDto> {

    @Override
    Class<RequestCheckPoint> getSourceClass() {
        return RequestCheckPoint.class;
    }

    @Override
    Class<RequestCheckPointDto> getTargetClass() {
        return RequestCheckPointDto.class;
    }
}
