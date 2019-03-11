package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceStage {

    private Long stageId;

    private Stage stage;

    private List<Operation> operations;

}
