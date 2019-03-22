package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.enums.OperationType;

import java.util.Comparator;

public interface Operation {

    OperationType type();

    int order();

    void run();

    String result();

    static Comparator<Operation> operationSorter() {
        return Comparator.comparingInt(Operation::order);
    }
}
