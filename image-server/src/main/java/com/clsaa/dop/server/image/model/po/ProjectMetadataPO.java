package com.clsaa.dop.server.image.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     用于和harbor进行projectMetadata映射的projectMetadata类
 * </p>
 */
@Getter
@Setter
public class ProjectMetadataPO {
    @JSONField(name = "public")
    private String _public;
    private String enable_content_trust;
    private String prevent_vul;
    private String severity;
    private String auto_scan;
}
