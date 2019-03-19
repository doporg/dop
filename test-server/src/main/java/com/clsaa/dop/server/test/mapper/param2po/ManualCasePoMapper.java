package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.ManualCaseParam;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@Component
public class ManualCasePoMapper extends AbstractCommonServiceMapper<ManualCaseParam, ManualCase> {

    @Override
    public Class<ManualCaseParam> getSourceClass() {
        return ManualCaseParam.class;
    }

    @Override
    public Class<ManualCase> getTargetClass() {
        return ManualCase.class;
    }

    @Override
    public Optional<ManualCase> convert(ManualCaseParam manualCaseParam) {
        return super.convert(manualCaseParam).map(po -> {
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
