package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * UserGroup
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T23:55:37.259Z")

public class UserGroup   {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("group_name")
  private String groupName;

  @JsonProperty("group_type")
  private Integer groupType;

  @JsonProperty("ldap_group_dn")
  private String ldapGroupDn;

  public UserGroup id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the user group
   * @return id
  **/
  @ApiModelProperty(value = "The ID of the user group")


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public UserGroup groupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  /**
   * The name of the user group
   * @return groupName
  **/
  @ApiModelProperty(value = "The name of the user group")


  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public UserGroup groupType(Integer groupType) {
    this.groupType = groupType;
    return this;
  }

  /**
   * The group type, 1 for LDAP group.
   * @return groupType
  **/
  @ApiModelProperty(value = "The group type, 1 for LDAP group.")


  public Integer getGroupType() {
    return groupType;
  }

  public void setGroupType(Integer groupType) {
    this.groupType = groupType;
  }

  public UserGroup ldapGroupDn(String ldapGroupDn) {
    this.ldapGroupDn = ldapGroupDn;
    return this;
  }

  /**
   * The DN of the LDAP group if group type is 1 (LDAP group).
   * @return ldapGroupDn
  **/
  @ApiModelProperty(value = "The DN of the LDAP group if group type is 1 (LDAP group).")


  public String getLdapGroupDn() {
    return ldapGroupDn;
  }

  public void setLdapGroupDn(String ldapGroupDn) {
    this.ldapGroupDn = ldapGroupDn;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserGroup userGroup = (UserGroup) o;
    return Objects.equals(this.id, userGroup.id) &&
        Objects.equals(this.groupName, userGroup.groupName) &&
        Objects.equals(this.groupType, userGroup.groupType) &&
        Objects.equals(this.ldapGroupDn, userGroup.ldapGroupDn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, groupName, groupType, ldapGroupDn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserGroup {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    groupName: ").append(toIndentedString(groupName)).append("\n");
    sb.append("    groupType: ").append(toIndentedString(groupType)).append("\n");
    sb.append("    ldapGroupDn: ").append(toIndentedString(ldapGroupDn)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

