package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The components overview of the image.
 */
@ApiModel(description = "The components overview of the image.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T08:16:31.961Z")

public class DetailedTagScanOverviewComponents {
  @JsonProperty("total")
  private Integer total = null;

  @JsonProperty("summary")
  @Valid
  private List<ComponentOverviewEntry> summary = null;

  public DetailedTagScanOverviewComponents total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Total number of the components in this image.
   * @return total
  **/
  @ApiModelProperty(value = "Total number of the components in this image.")


  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public DetailedTagScanOverviewComponents summary(List<ComponentOverviewEntry> summary) {
    this.summary = summary;
    return this;
  }

  public DetailedTagScanOverviewComponents addSummaryItem(ComponentOverviewEntry summaryItem) {
    if (this.summary == null) {
      this.summary = new ArrayList<ComponentOverviewEntry>();
    }
    this.summary.add(summaryItem);
    return this;
  }

  /**
   * List of number of components of different severities.
   * @return summary
  **/
  @ApiModelProperty(value = "List of number of components of different severities.")

  @Valid

  public List<ComponentOverviewEntry> getSummary() {
    return summary;
  }

  public void setSummary(List<ComponentOverviewEntry> summary) {
    this.summary = summary;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetailedTagScanOverviewComponents detailedTagScanOverviewComponents = (DetailedTagScanOverviewComponents) o;
    return Objects.equals(this.total, detailedTagScanOverviewComponents.total) &&
        Objects.equals(this.summary, detailedTagScanOverviewComponents.summary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, summary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetailedTagScanOverviewComponents {\n");

    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
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

