package com.clsaa.dop.server.test.service.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.enums.ParamClass;
import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.param.RequestParamCreateParam;
import com.clsaa.dop.server.test.model.param.RequestScriptParam;
import com.clsaa.dop.server.test.model.vo.ImportApiVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Component
public class ImportService {

    public static final String HOST_KEY = "host";
    public static final String BASE_PATH_KEY = "basePath";
    public static final String PATHS_KEY = "paths";
    public static final String PARAMS_KEY = "parameters";
    public static final String PARAM_TYPE_KEY = "in";

    public ImportApiVo doImport(String openApi) {
        JSONObject apiJson = JSON.parseObject(openApi);

        List<CaseParamRef> caseParams = new ArrayList<>();
        String host = apiJson.getString(HOST_KEY);
        addCaseParam("host", host, caseParams);
        String basePath = apiJson.getString(BASE_PATH_KEY);
        addCaseParam("basePath", basePath, caseParams);

        JSONObject pathsJson = apiJson.getJSONObject(PATHS_KEY);
        List<RequestScriptParam> requestScriptParams = new ArrayList<>();
        AtomicReference<Integer> order = new AtomicReference<>(0);
        pathsJson.getInnerMap().forEach(
                (rawUrl, object) ->
                    ensureJson(object)
                    .getInnerMap()
                    .forEach((method, apiInfo) -> {
                        RequestScriptParam requestScript = new RequestScriptParam();
                        HttpMethod httpMethod = HttpMethod.from(method);
                        JSONObject infoObject = ensureJson(apiInfo);

                        List<RequestParamCreateParam> requestParams = new ArrayList<>();
                        infoObject.getJSONArray(PARAMS_KEY).forEach(param -> {
                            JSONObject paramJson = ensureJson(param);
                            ParamClass paramClass = ParamClass.from(paramJson.getString(PARAM_TYPE_KEY));
                            String name = paramJson.getString("name");
                            RequestParamCreateParam requestParam = RequestParamCreateParam.builder()
                                    .paramClass(paramClass)
                                    .name(name)
                                    .value("")
                                    .build();
                            requestParams.add(requestParam);
                        });

                        String url = "http://" + "${host}" + "${basePath}";
                        if (rawUrl.startsWith("/")) {
                            url += rawUrl.substring(1);
                        }else {
                            url += rawUrl;
                        }

                        requestScript.setRawUrl(url);
                        requestScript.setHttpMethod(httpMethod);
                        requestScript.setRequestParams(requestParams);
                        requestScript.setRequestHeaders(new ArrayList<>());
                        requestScript.setRequestCheckPoints(new ArrayList<>());
                        requestScript.setResultParams(new ArrayList<>());
                        requestScript.setRequestBody("");
                        requestScript.setRetryTimes(0);
                        requestScript.setRetryInterval(0L);
                        requestScript.setOperationType(OperationType.REQUEST);

                        requestScript.setOrder(order.getAndUpdate(integer -> integer + 1));
                        requestScriptParams.add(requestScript);
                    })
        );

        return ImportApiVo.builder()
                .caseParams(caseParams)
                .requestScripts(requestScriptParams).build();
    }

    private JSONObject ensureJson(Object value) {
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }

        return (JSONObject) JSON.toJSON(value);
    }

    private void addCaseParam(String ref, String value, List<CaseParamRef> params) {
        CaseParamRef newParam = CaseParamRef.builder().ref(ref).value(value).build();
        params.add(newParam);
    }
}
