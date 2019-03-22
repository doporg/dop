package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestResultParam;
import com.clsaa.dop.server.test.model.po.UrlResultParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class ResultParamPoMapper extends AbstractCommonServiceMapper<RequestResultParam, UrlResultParam> {

    @Override
    public Class<RequestResultParam> getSourceClass() {
        return RequestResultParam.class;
    }

    @Override
    public Class<UrlResultParam> getTargetClass() {
        return UrlResultParam.class;
    }

    @Override
    public Optional<UrlResultParam> convert(RequestResultParam requestResultParam) {
        return super.convert(requestResultParam).map(po -> {
            LocalDateTime current = LocalDateTime.now();
            po.setCtime(current);
            po.setMtime(current);
            //todo set user
            po.setCuser(110L);
            po.setMuser(110L);
            return po;
        });
    }
}
