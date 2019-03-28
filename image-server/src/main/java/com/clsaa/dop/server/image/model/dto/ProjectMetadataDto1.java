package com.clsaa.dop.server.image.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     项目基本信息传输对象
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Getter
@Setter
public class ProjectMetadataDto1 {
    private String publicStatus;
    private String enableContentTrust;
    private String preventVul;
    private String severity;
    private String autoScan;
}
