package com.clsaa.dop.server.image.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     项目信息的视图层对象
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Getter
@Setter
public class ProjectMetadataVO {
    private String publicStatus;
    private String enableContentTrust;
    private String preventVul;
    private String severity;
    private String autoScan;
}
