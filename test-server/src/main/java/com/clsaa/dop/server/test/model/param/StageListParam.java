package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.model.po.InterfaceStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 29/03/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageListParam {

    private List<InterfaceStageParam> stageParams;
}
