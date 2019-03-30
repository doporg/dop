package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.model.context.ExecuteContext;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.clsaa.dop.server.test.doExecute.Operation.operationSorter;
import static com.clsaa.dop.server.test.util.UserUtils.dateAndUser;
import static java.util.Objects.nonNull;

/**
 * 完成测试脚本的执行
 * @author xihao
 * @version 1.0
 * @since 20/03/2019
 */
public class TestManager {

    public static final String FAIL_RESULT = "fail";
    public static final String SUCCESS_RESULT = "success";

    public static InterfaceCaseDto execute(InterfaceCaseDto interfaceCaseDto) {
        ExecuteContext executeContext = ExecuteContext.builder()
                                                .interfaceExecuteLog(initExecuteLog(interfaceCaseDto))
                                                .interfaceCaseDto(interfaceCaseDto).build();
        List<InterfaceStageDto> stages = interfaceCaseDto.getStages();
        sort(stages, new StageSorter())
                .forEach(stage -> doExecute(stage, executeContext));
        executeContext.logAfterExecution();
        return interfaceCaseDto;
    }

    private static InterfaceExecuteLog initExecuteLog(InterfaceCaseDto interfaceCaseDto) {
        return (InterfaceExecuteLog) dateAndUser().apply(
                InterfaceExecuteLog.builder()
                        .operationExecuteLogs(new ArrayList<>())
                        .caseId(interfaceCaseDto.getId())
                        .begin(LocalDateTime.now())
                        .build()
        );
    }

    private static void doExecute(InterfaceStageDto stage, ExecuteContext executeContext) {
        if (nonNull(stage)) {
            executeContext.setCurrentStage(stage.getStage());
            sort(stage.getOperations(), operationSorter())
                    .forEach(operation -> operation.run(executeContext));
        }
    }

    private static <T> List<T> sort(List<T> data, Comparator<T> sorter) {
        data.sort(sorter);
        return data;
    }

}

