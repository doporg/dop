package com.clsaa.dop.server.code.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBo {

    private int id;
    private String description;
    private String name;
    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private int star_count;
    private int forks_count;
    private String[] tag_list;
    private OwnerBo owner;

}
