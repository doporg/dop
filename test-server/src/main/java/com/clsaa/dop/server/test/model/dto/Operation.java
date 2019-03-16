package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.OperationType;

public interface Operation {

    void run();

    OperationType type();

}
