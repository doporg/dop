package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;

import java.util.Comparator;
import java.util.List;

import static com.clsaa.dop.server.test.doExecute.Operation.operationSorter;
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
        List<InterfaceStageDto> stages = interfaceCaseDto.getStages();
        sort(stages, new StageSorter())
                .forEach(stage -> doExecute(stage));
        return interfaceCaseDto;
    }

    private static void doExecute(InterfaceStageDto stage) {
        if (nonNull(stage)) {
            sort(stage.getOperations(), operationSorter())
                    .forEach(Operation::run);
        }
    }

    private static <T> List<T> sort(List<T> data, Comparator<T> sorter) {
        data.sort(sorter);
        return data;
    }

}

