package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * ComponentOverviewEntry对应的harbor类
 *
 * @author xzt
 * @since 2019-3-30
 */

@Setter
@Getter
public class ComponentOverviewEntry {
    @JsonProperty("severity")
    private Integer severity;

    @JsonProperty("count")
    private Integer count;
}

