package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.doExecute.context.ExecuteContext;
import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.clsaa.dop.server.test.doExecute.Operation.operationSorter;
import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;
import static java.util.Objects.nonNull;

/**
 * @author xihao
 * @version 1.0
 * @since 15/04/2019
 */
public class ExecuteTask implements Runnable {

    private InterfaceCaseDto interfaceCaseDto;

    private Long userId;

    public ExecuteTask(InterfaceCaseDto interfaceCaseDto, Long userId) {
        this.interfaceCaseDto = interfaceCaseDto;
        this.userId = userId;
    }

    @Override
    public void run() {
        Long threadOldUser = UserManager.getCurrentUserId();
        if (userId != null) {
            UserManager.setUserId(userId);
        }

        doExecute();

        UserManager.setUserId(threadOldUser);
    }

    private void doExecute() {
        ExecuteContext executeContext = ExecuteContext.builder()
                .interfaceExecuteLog(initExecuteLog(interfaceCaseDto))
                .interfaceCaseDto(interfaceCaseDto)
                .caseParams(interfaceCaseDto.getParamsMap())
                .build();
        List<InterfaceStageDto> stages = interfaceCaseDto.getStages();
        sort(stages, new StageSorter())
                .forEach(stage -> doExecute(stage, executeContext));
        executeContext.logAfterExecution();
    }

    private InterfaceExecuteLog initExecuteLog(InterfaceCaseDto interfaceCaseDto) {
        return (InterfaceExecuteLog) dateAndUser().apply(
                InterfaceExecuteLog.builder()
                        .operationExecuteLogs(new ArrayList<>())
                        .caseId(interfaceCaseDto.getId())
                        .begin(LocalDateTime.now())
                        .build()
        );
    }

    private <T> List<T> sort(List<T> data, Comparator<T> sorter) {
        data.sort(sorter);
        return data;
    }

    private void doExecute(InterfaceStageDto stage, ExecuteContext executeContext) {
        if (nonNull(stage)) {
            executeContext.setCurrentStage(stage.getStage());
            sort(stage.getOperations(), operationSorter())
                    .forEach(operation -> operation.run(executeContext));
        }
    }

}
