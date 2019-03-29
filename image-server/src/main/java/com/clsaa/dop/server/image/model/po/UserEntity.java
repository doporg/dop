package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * UserEntity
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T23:55:37.259Z")

public class UserEntity   {
  @JsonProperty("user_id")
  private Integer userId;

  @JsonProperty("username")
  private String username;

  public UserEntity userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * The ID of the user.
   * @return userId
  **/
  @ApiModelProperty(value = "The ID of the user.")


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public UserEntity username(String username) {
    this.username = username;
    return this;
  }

  /**
   * The name of the user.
   * @return username
  **/
  @ApiModelProperty(value = "The name of the user.")


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserEntity userEntity = (UserEntity) o;
    return Objects.equals(this.userId, userEntity.userId) &&
        Objects.equals(this.username, userEntity.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, username);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserEntity {\n");

    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

