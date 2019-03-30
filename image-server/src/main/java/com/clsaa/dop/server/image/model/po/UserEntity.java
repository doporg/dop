package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * UserEntity
 * @author xzt
 * @since 2019-3-30
 */

@Getter
@Setter
public class UserEntity   {
  @JsonProperty("user_id")
  private Integer userId;

  @JsonProperty("username")
  private String username;

}

