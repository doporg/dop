package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.enums.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.clsaa.dop.server.test.doExecute.TestManager.FAIL_RESULT;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceStageDto {

    // ----------- main property ---------
    private Stage stage;

    private List<RequestScriptDto> requestScripts;

    private List<WaitOperationDto> waitOperations;

    private List<Operation> operations;

    private boolean success = true;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    public boolean executeSuccess() {
        return !CollectionUtils.isEmpty(operations)
                &&
                operations.stream().noneMatch(operation -> operation.result().equals(FAIL_RESULT));
    }

    public boolean fail() {
        return !executeSuccess();
    }

    public static InterfaceStageDto emptyStage(Stage stage) {
        return InterfaceStageDto.builder().stage(stage)
                .operations(new ArrayList<>())
                .requestScripts(new ArrayList<>())
                .waitOperations(new ArrayList<>()).build();
    }
}
