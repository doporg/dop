package com.clsaa.dop.server.test.util;

import com.clsaa.dop.server.test.dao.InterfaceCaseLogRepository;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
public class ExecutionLogUtils {

    private static InterfaceCaseLogRepository logRepository;

    static {
        logRepository = Services.of(InterfaceCaseLogRepository.class);
    }

    public static void saveLog(InterfaceExecuteLog interfaceExecuteLog) {
        logRepository.save(interfaceExecuteLog);
    }
}
