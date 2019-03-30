package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * UserGroup
 * @author xzt
 * @since 2019-3-30
 */


@Getter
@Setter
public class UserGroup   {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("group_name")
  private String groupName;

  @JsonProperty("group_type")
  private Integer groupType;

  @JsonProperty("ldap_group_dn")
  private String ldapGroupDn;

}

