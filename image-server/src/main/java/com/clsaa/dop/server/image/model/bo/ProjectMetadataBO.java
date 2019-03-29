package com.clsaa.dop.server.image.model.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     项目基本权限信息业务类
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Setter
@Getter
@ToString
public class ProjectMetadataBO {
    private String publicStatus;
    private String enableContentTrust;
    private String preventVul;
    private String severity;
    private String autoScan;
}
