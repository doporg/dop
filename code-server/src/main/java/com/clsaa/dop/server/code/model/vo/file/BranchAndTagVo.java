package com.clsaa.dop.server.code.model.vo.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchAndTagVo {

    private List<String> branches;
    private List<String> tags;
}
