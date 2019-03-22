package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.WaitOperationParam;
import com.clsaa.dop.server.test.model.po.WaitOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class WaitOperationPoMapper extends AbstractCommonServiceMapper<WaitOperationParam, WaitOperation> {

    @Override
    public Class<WaitOperationParam> getSourceClass() {
        return WaitOperationParam.class;
    }

    @Override
    public Class<WaitOperation> getTargetClass() {
        return WaitOperation.class;
    }

    @Override
    public Optional<WaitOperation> convert(WaitOperationParam waitOperationParam) {
        return super.convert(waitOperationParam).map(waitOperation -> {
            LocalDateTime current = LocalDateTime.now();
            waitOperation.setCtime(current);
            waitOperation.setMtime(current);
            //todo set user
            waitOperation.setCuser(110L);
            waitOperation.setMuser(110L);
            return waitOperation;
        });
    }

}
