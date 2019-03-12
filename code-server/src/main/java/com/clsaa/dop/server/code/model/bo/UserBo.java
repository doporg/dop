package com.clsaa.dop.server.code.model.bo;

import lombok.*;

/**
 * @author wsy
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBo {

    private int id;
    private String username;
    private String name;
    private String state;
    private String avatar_url;
    private String web_url;

}
