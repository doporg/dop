package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * RetagReq
 */

@Setter
@Getter
public class RetagReq {
    @JsonProperty("tag")
    private String tag;

    @JsonProperty("src_image")
    private String srcImage;

    @JsonProperty("override")
    private Boolean override;
}

