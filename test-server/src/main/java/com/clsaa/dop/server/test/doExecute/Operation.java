package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.model.context.ExecuteContext;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import com.clsaa.dop.server.test.util.UserUtils;

import java.time.LocalDateTime;
import java.util.Comparator;

import static com.clsaa.dop.server.test.doExecute.TestManager.SUCCESS_RESULT;
import static com.clsaa.dop.server.test.enums.OperationType.REQUEST;

public interface Operation {

    OperationType type();

    int order();

    void run(ExecuteContext executeContext);

    String result();

    static Comparator<Operation> operationSorter() {
        return Comparator.comparingInt(Operation::order);
    }

    default OperationExecuteLog initOperationLog(ExecuteContext executeContext) {
        return (OperationExecuteLog) UserUtils.dateAndUser().apply(
                OperationExecuteLog.builder()
                        .interfaceExecuteLog(executeContext.getInterfaceExecuteLog())
                        .stage(executeContext.getCurrentStage())
                        .begin(LocalDateTime.now())
                        .operationType(type())
                        .order(order())
                        .build()
        );
    }

    default OperationExecuteLog endOperationLog(OperationExecuteLog executeLog) {
        executeLog.setEnd(LocalDateTime.now());
        executeLog.setSuccess(SUCCESS_RESULT.equals(result()));
        return executeLog;
    }
}
