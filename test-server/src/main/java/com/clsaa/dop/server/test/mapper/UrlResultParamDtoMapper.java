package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.UrlResultParamDto;
import com.clsaa.dop.server.test.model.po.UrlResultParam;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class UrlResultParamDtoMapper extends AbstractCommonServiceMapper<UrlResultParam, UrlResultParamDto> {

    @Override
    Class<UrlResultParam> getSourceClass() {
        return UrlResultParam.class;
    }

    @Override
    Class<UrlResultParamDto> getTargetClass() {
        return UrlResultParamDto.class;
    }
}
