package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.OperationType;

public class WaitOperationDto implements Operation{

    // ms unit
    private int waitTime;

    @Override
    public void run() {
        //todo wait
    }

    @Override
    public OperationType type() {
        return OperationType.WAIT;
    }
}
