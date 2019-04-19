package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.RequestCheckPointDto;
import com.clsaa.dop.server.test.model.dto.RequestHeaderDto;
import com.clsaa.dop.server.test.model.dto.RequestScriptDto;
import com.clsaa.dop.server.test.model.dto.UrlResultParamDto;
import com.clsaa.dop.server.test.model.po.RequestCheckPoint;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import com.clsaa.dop.server.test.model.po.RequestScript;
import com.clsaa.dop.server.test.model.po.UrlResultParam;
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

    @Override
    public Optional<RequestScript> inverseConvert(RequestScriptDto requestScriptDto) {
        if (requestScriptDto.getOrder() < 0) {
            // 无效的请求脚本 【前端参数有order<0的无效数据】
            return Optional.empty();
        }

        List<RequestHeader> headers = headerDtoMapper.inverseConvert(requestScriptDto.getRequestHeaders());
        List<RequestCheckPoint> checkPoints = checkPointDtoMapper.inverseConvert(requestScriptDto.getRequestCheckPoints());
        List<UrlResultParam> resultParams = resultParamDtoMapper.inverseConvert(requestScriptDto.getResultParams());
        return super.inverseConvert(requestScriptDto)
                .map(UserManager.newInfoIfNotExists())
                .map(fillScriptProperties(headers, checkPoints, resultParams))
                ;
    }

    private Function<RequestScript, RequestScript> fillScriptProperties(List<RequestHeader> headers,
                                                                        List<RequestCheckPoint> checkPoints,
                                                                        List<UrlResultParam> resultParams) {
        return requestScript -> {
            headers.forEach(header -> header.setRequestScript(requestScript));
            checkPoints.forEach(checkPoint -> checkPoint.setRequestScript(requestScript));
            resultParams.forEach(resultParam -> resultParam.setRequestScript(requestScript));

            requestScript.setRequestHeaders(headers);
            requestScript.setRequestCheckPoints(checkPoints);
            requestScript.setResultParams(resultParams);

            return requestScript;
        };
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
