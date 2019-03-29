package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * ProjectMember的harbor对应实体类
 * @author  xzt
 * @since 2019-3-25
 */

@Setter
@Getter
public class ProjectMember   {
  @JsonProperty("role_id")
  private Integer roleId;

  @JsonProperty("member_user")
  private UserEntity memberUser;

  @JsonProperty("member_group")
  private UserGroup memberGroup;

}

