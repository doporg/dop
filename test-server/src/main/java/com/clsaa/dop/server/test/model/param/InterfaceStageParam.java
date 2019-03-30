package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.Stage;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
public class InterfaceStageParam {

    @Min(1)
    @NotNull
    private Long caseId;

    @NotNull
    private Stage stage;

    //todo custom validator
    @Valid
    private List<RequestScriptParam> requestScripts;

    @Valid
    private List<WaitOperationParam> waitOperations;
}
