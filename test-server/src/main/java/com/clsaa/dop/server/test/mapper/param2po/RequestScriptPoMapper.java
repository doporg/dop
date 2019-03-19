package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestScriptParam;
import com.clsaa.dop.server.test.model.po.RequestCheckPoint;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import com.clsaa.dop.server.test.model.po.RequestScript;
import com.clsaa.dop.server.test.model.po.UrlResultParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestScriptPoMapper extends AbstractCommonServiceMapper<RequestScriptParam, RequestScript> {

    @Autowired
    private RequestHeaderPoMapper requestHeaderPoMapper;

    @Autowired
    private CheckPointPoMapper checkPointPoMapper;

    @Autowired
    private ResultParamPoMapper resultParamPoMapper;

    @Override
    public Class<RequestScriptParam> getSourceClass() {
        return RequestScriptParam.class;
    }

    @Override
    public Class<RequestScript> getTargetClass() {
        return RequestScript.class;
    }

    @Override
    public Optional<RequestScript> convert(RequestScriptParam requestScriptParam) {
        List<RequestHeader> requestHeaders = requestHeaderPoMapper.convert(requestScriptParam.getRequestHeaders());
        List<RequestCheckPoint> checkPoints = checkPointPoMapper.convert(requestScriptParam.getRequestCheckPoints());
        List<UrlResultParam> resultParams = resultParamPoMapper.convert(requestScriptParam.getResultParams());
        return super.convert(requestScriptParam).map(requestScript -> {
            requestScript.setRequestHeaders(requestHeaders);
            requestScript.setRequestCheckPoints(checkPoints);
            requestScript.setResultParams(resultParams);

            //inject RequestScript into joined Objects
            requestHeaders.forEach(requestHeader -> requestHeader.setRequestScript(requestScript));
            checkPoints.forEach(checkPoint -> checkPoint.setRequestScript(requestScript));
            resultParams.forEach(urlResultParam -> urlResultParam.setRequestScript(requestScript));

            return requestScript;
        });
    }
}
