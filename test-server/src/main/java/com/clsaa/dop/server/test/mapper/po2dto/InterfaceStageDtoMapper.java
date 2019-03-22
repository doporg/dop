package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.dto.RequestScriptDto;
import com.clsaa.dop.server.test.model.dto.WaitOperationDto;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class InterfaceStageDtoMapper extends AbstractCommonServiceMapper<InterfaceStage, InterfaceStageDto> {

    @Autowired
    private RequestScriptDtoMapper requestScriptDtoMapper;

    @Autowired
    private WaitOperationDtoMapper waitOperationDtoMapper;

    @Override
    public Class<InterfaceStage> getSourceClass() {
        return InterfaceStage.class;
    }

    @Override
    public Class<InterfaceStageDto> getTargetClass() {
        return InterfaceStageDto.class;
    }

    @Override
    public Optional<InterfaceStageDto> convert(InterfaceStage interfaceStage) {
        return super.convert(interfaceStage).map(interfaceStageDto -> {
            List<RequestScriptDto> requestScriptDtos = requestScriptDtoMapper.convert(interfaceStage.getRequestScripts());
            List<WaitOperationDto> waitOperationDtos = waitOperationDtoMapper.convert(interfaceStage.getWaitOperations());
            interfaceStageDto.setRequestScripts(requestScriptDtos);
            interfaceStageDto.setWaitOperations(waitOperationDtos);
            List<Operation> operations = new ArrayList<>();
            operations.addAll(requireNonNull(requestScriptDtos));
            operations.addAll(requireNonNull(waitOperationDtos));
            interfaceStageDto.setOperations(operations);
            return interfaceStageDto;
        });
    }
}
