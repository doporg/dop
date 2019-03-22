package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * 完成测试脚本的执行
 * @author xihao
 * @version 1.0
 * @since 20/03/2019
 */
public class TestManager {

    private static final String FAIL_RESULT = "fail";
    private static final String SUCCESS_RESULT = "success";

    public static String execute(InterfaceCaseDto interfaceCaseDto) {
        if (isNull(interfaceCaseDto)) {
            //todo define Result Info
            return FAIL_RESULT;
        }

        List<InterfaceStageDto> stages = interfaceCaseDto.getStages();

        return FAIL_RESULT;
    }
}
