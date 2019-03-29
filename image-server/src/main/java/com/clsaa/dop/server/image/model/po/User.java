package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("realname")
    private String realname;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("deleted")
    private Boolean deleted;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("role_id")
    private Integer roleId;

    @JsonProperty("has_admin_role")
    private Boolean hasAdminRole;

    @JsonProperty("reset_uuid")
    private String resetUuid;

    @JsonProperty("Salt")
    private String salt;

    @JsonProperty("creation_time")
    private String creationTime;

    @JsonProperty("update_time")
    private String updateTime;
}
