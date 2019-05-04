package com.clsaa.dop.server.test.service.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.param.InterfaceStageParam;
import com.clsaa.dop.server.test.model.param.RequestScriptParam;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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

    public String doImport(String openApi) {
        JSONObject apiJson = JSON.parseObject(openApi);

        List<CaseParamRef> params = new ArrayList<>();
        String host = apiJson.getString(HOST_KEY);
        addCaseParam("host", host, params);

        String basePath = apiJson.getString(BASE_PATH_KEY);
        InterfaceStageParam stage = new InterfaceStageParam();
        JSONObject pathsJson = apiJson.getJSONObject(PATHS_KEY);
        int order = 0;
        pathsJson.getInnerMap().forEach((key, object) -> {
            if (object instanceof JSONObject) {
                ((JSONObject) object).getInnerMap().forEach((method, apiInfo) -> {
                    RequestScriptParam requestScript = new RequestScriptParam();
                    HttpMethod httpMethod = HttpMethod.from(method);
                    JSONObject infoObject = (JSONObject) apiInfo;
                    
                });
            }
        });

        return "";
    }

    private void addCaseParam(String ref, String value, List<CaseParamRef> params) {
        CaseParamRef newParam = CaseParamRef.builder().ref(ref).value(value).build();
        params.add(newParam);
    }
}
