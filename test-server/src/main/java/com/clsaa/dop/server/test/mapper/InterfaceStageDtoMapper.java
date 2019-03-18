package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.dto.RequestScriptDto;
import com.clsaa.dop.server.test.model.dto.WaitOperationDto;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
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
public class InterfaceStageDtoMapper extends AbstractCommonServiceMapper<InterfaceStage, InterfaceStageDto> {

    @Autowired
    private RequestScriptDtoMapper requestScriptDtoMapper;

    @Autowired
    private WaitOperationDtoMapper waitOperationDtoMapper;

    @Override
    Class<InterfaceStage> getSourceClass() {
        return InterfaceStage.class;
    }

    @Override
    Class<InterfaceStageDto> getTargetClass() {
        return InterfaceStageDto.class;
    }

    @Override
    public Optional<InterfaceStageDto> convert(InterfaceStage interfaceStage) {
        return super.convert(interfaceStage).map(interfaceStageDto -> {
            List<RequestScriptDto> requestScriptDtos = requestScriptDtoMapper.convert(interfaceStage.getRequestScripts());
            List<WaitOperationDto> waitOperationDtos = waitOperationDtoMapper.convert(interfaceStage.getWaitOperations());
            interfaceStageDto.setRequestScripts(requestScriptDtos);
            interfaceStageDto.setWaitOperations(waitOperationDtos);
            return interfaceStageDto;
        });
    }
}
