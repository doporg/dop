package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.RequestCheckPointDto;
import com.clsaa.dop.server.test.model.dto.RequestHeaderDto;
import com.clsaa.dop.server.test.model.dto.RequestScriptDto;
import com.clsaa.dop.server.test.model.dto.UrlResultParamDto;
import com.clsaa.dop.server.test.model.po.RequestScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestScriptDtoMapper extends AbstractCommonServiceMapper<RequestScript, RequestScriptDto> {

    @Autowired
    private RequestHeaderDtoMapper headerDtoMapper;

    @Autowired
    private RequestCheckPointDtoMapper checkPointDtoMapper;

    @Autowired
    private UrlResultParamDtoMapper resultParamDtoMapper;

    @Override
    public Class<RequestScript> getSourceClass() {
        return RequestScript.class;
    }

    @Override
    public Class<RequestScriptDto> getTargetClass() {
        return RequestScriptDto.class;
    }

    @Override
    public Optional<RequestScriptDto> convert(RequestScript requestScript) {
        List<RequestHeaderDto> headerDtos = headerDtoMapper.convert(requestScript.getRequestHeaders());
        List<RequestCheckPointDto> checkPointDtos = checkPointDtoMapper.convert(requestScript.getRequestCheckPoints());
        List<UrlResultParamDto> resultParamDtos = resultParamDtoMapper.convert(requestScript.getResultParams());
        return super.convert(requestScript).map(fillProperties(headerDtos,checkPointDtos,resultParamDtos));
    }

    private Function<RequestScriptDto,RequestScriptDto> fillProperties(List<RequestHeaderDto> headerDtos,
                               List<RequestCheckPointDto> checkPointDtos, List<UrlResultParamDto> resultParamDtos) {
        return requestScriptDto -> {
            requestScriptDto.setRequestHeaders(headerDtos);
            Map<String, String> headersMap = headerDtos.stream()
                    .collect(Collectors.toMap(RequestHeaderDto::getName, RequestHeaderDto::getValue));
            requestScriptDto.setHeadersMap(headersMap);
            requestScriptDto.setRequestCheckPoints(checkPointDtos);
            requestScriptDto.setResultParams(resultParamDtos);
            return requestScriptDto;
        };
    }

}
