package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceExecuteLogDto;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
@Component
public class InterfaceExecuteLogDtoMapper extends AbstractCommonServiceMapper<InterfaceExecuteLog, InterfaceExecuteLogDto> {

    @Autowired
    private OperationLogDtoMapper operationLogDtoMapper;

    @Override
    public Class<InterfaceExecuteLog> getSourceClass() {
        return InterfaceExecuteLog.class;
    }

    @Override
    public Class<InterfaceExecuteLogDto> getTargetClass() {
        return InterfaceExecuteLogDto.class;
    }

    @Override
    public Optional<InterfaceExecuteLogDto> convert(InterfaceExecuteLog interfaceExecuteLog) {
        return super.convert(interfaceExecuteLog).map(interfaceExecuteLogDto -> {
            interfaceExecuteLogDto.setOperationExecuteLogs(operationLogDtoMapper.convert(interfaceExecuteLog.getOperationExecuteLogs()));
            return interfaceExecuteLogDto;
        });
    }
}
