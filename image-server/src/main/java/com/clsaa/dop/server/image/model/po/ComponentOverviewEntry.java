package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ComponentOverviewEntry
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T08:16:31.961Z")

public class ComponentOverviewEntry {
  @JsonProperty("severity")
  private Integer severity = null;

  @JsonProperty("count")
  private Integer count = null;

  public ComponentOverviewEntry severity(Integer severity) {
    this.severity = severity;
    return this;
  }

  /**
   * 1-None/Negligible, 2-Unknown, 3-Low, 4-Medium, 5-High
   * @return severity
  **/
  @ApiModelProperty(value = "1-None/Negligible, 2-Unknown, 3-Low, 4-Medium, 5-High")


  public Integer getSeverity() {
    return severity;
  }

  public void setSeverity(Integer severity) {
    this.severity = severity;
  }

  public ComponentOverviewEntry count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * number of the components with certain severity.
   * @return count
  **/
  @ApiModelProperty(value = "number of the components with certain severity.")


  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentOverviewEntry componentOverviewEntry = (ComponentOverviewEntry) o;
    return Objects.equals(this.severity, componentOverviewEntry.severity) &&
        Objects.equals(this.count, componentOverviewEntry.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(severity, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponentOverviewEntry {\n");

    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

