package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Project
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T08:16:31.961Z")

public class Project   {
  @JsonProperty("project_id")
  private Integer projectId = null;

  @JsonProperty("owner_id")
  private Integer ownerId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("creation_time")
  private String creationTime = null;

  @JsonProperty("update_time")
  private String updateTime = null;

  @JsonProperty("deleted")
  private Boolean deleted = null;

  @JsonProperty("owner_name")
  private String ownerName = null;

  @JsonProperty("togglable")
  private Boolean togglable = null;

  @JsonProperty("current_user_role_id")
  private Integer currentUserRoleId = null;

  @JsonProperty("repo_count")
  private Integer repoCount = null;

  @JsonProperty("chart_count")
  private Integer chartCount = null;

  @JsonProperty("metadata")
  private ProjectMetadata metadata = null;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Project project = (Project) o;
    return Objects.equals(this.projectId, project.projectId) &&
        Objects.equals(this.ownerId, project.ownerId) &&
        Objects.equals(this.name, project.name) &&
        Objects.equals(this.creationTime, project.creationTime) &&
        Objects.equals(this.updateTime, project.updateTime) &&
        Objects.equals(this.deleted, project.deleted) &&
        Objects.equals(this.ownerName, project.ownerName) &&
        Objects.equals(this.togglable, project.togglable) &&
        Objects.equals(this.currentUserRoleId, project.currentUserRoleId) &&
        Objects.equals(this.repoCount, project.repoCount) &&
        Objects.equals(this.chartCount, project.chartCount) &&
        Objects.equals(this.metadata, project.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectId, ownerId, name, creationTime, updateTime, deleted, ownerName, togglable, currentUserRoleId, repoCount, chartCount, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Project {\n");
    sb.append("    projectId: ").append(toIndentedString(projectId)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    ownerName: ").append(toIndentedString(ownerName)).append("\n");
    sb.append("    togglable: ").append(toIndentedString(togglable)).append("\n");
    sb.append("    currentUserRoleId: ").append(toIndentedString(currentUserRoleId)).append("\n");
    sb.append("    repoCount: ").append(toIndentedString(repoCount)).append("\n");
    sb.append("    chartCount: ").append(toIndentedString(chartCount)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

