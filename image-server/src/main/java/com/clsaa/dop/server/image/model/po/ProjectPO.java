package com.clsaa.dop.server.image.model.po;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>用于和harbor进行project映射的project类
 * </p>
 * @author  xzt
 * @since  2019-03-23
 */
@Setter
@Getter
public class ProjectPO {
    private Integer project_id;
    private Integer owner_id;
    private String name;
    private String creation_time;
    private String update_time;
    private boolean deleted;
    private String owner_name;
    private boolean togglable;
    private Integer current_user_role_id;
    private Integer repo_count;
    private Integer chart_count;
    private ProjectMetadataPO metadata;
}
