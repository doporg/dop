package com.clsaa.dop.server.code.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListBo {

    private String name;
    private String description;
    private String path_with_namespace;
}