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
public class OwnerBo {
    private int id;
    private String name;
    private String username;
    private String state;
    private String avatar_url;
    private String web_url;

}
