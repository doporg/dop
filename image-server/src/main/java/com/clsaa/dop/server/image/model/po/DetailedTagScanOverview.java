package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * The overview of the scan result.  This is an optional property.
 */
@ApiModel(description = "The overview of the scan result.  This is an optional property.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T08:16:31.961Z")

public class DetailedTagScanOverview {
  @JsonProperty("digest")
  private String digest = null;

  @JsonProperty("scan_status")
  private String scanStatus = null;

  @JsonProperty("job_id")
  private Integer jobId = null;

  @JsonProperty("severity")
  private Integer severity = null;

  @JsonProperty("details_key")
  private String detailsKey = null;

  @JsonProperty("components")
  private DetailedTagScanOverviewComponents components = null;

  public DetailedTagScanOverview digest(String digest) {
    this.digest = digest;
    return this;
  }

  /**
   * The digest of the image.
   * @return digest
  **/
  @ApiModelProperty(value = "The digest of the image.")


  public String getDigest() {
    return digest;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public DetailedTagScanOverview scanStatus(String scanStatus) {
    this.scanStatus = scanStatus;
    return this;
  }

  /**
   * The status of the scan job, it can be \"pendnig\", \"running\", \"finished\", \"error\".
   * @return scanStatus
  **/
  @ApiModelProperty(value = "The status of the scan job, it can be \"pendnig\", \"running\", \"finished\", \"error\".")


  public String getScanStatus() {
    return scanStatus;
  }

  public void setScanStatus(String scanStatus) {
    this.scanStatus = scanStatus;
  }

  public DetailedTagScanOverview jobId(Integer jobId) {
    this.jobId = jobId;
    return this;
  }

  /**
   * The ID of the job on jobservice to scan the image.
   * @return jobId
  **/
  @ApiModelProperty(value = "The ID of the job on jobservice to scan the image.")


  public Integer getJobId() {
    return jobId;
  }

  public void setJobId(Integer jobId) {
    this.jobId = jobId;
  }

  public DetailedTagScanOverview severity(Integer severity) {
    this.severity = severity;
    return this;
  }

  /**
   * 0-Not scanned, 1-Negligible, 2-Unknown, 3-Low, 4-Medium, 5-High
   * @return severity
  **/
  @ApiModelProperty(value = "0-Not scanned, 1-Negligible, 2-Unknown, 3-Low, 4-Medium, 5-High")


  public Integer getSeverity() {
    return severity;
  }

  public void setSeverity(Integer severity) {
    this.severity = severity;
  }

  public DetailedTagScanOverview detailsKey(String detailsKey) {
    this.detailsKey = detailsKey;
    return this;
  }

  /**
   * The top layer name of this image in Clair, this is for calling Clair API to get the vulnerability list of this image.
   * @return detailsKey
  **/
  @ApiModelProperty(value = "The top layer name of this image in Clair, this is for calling Clair API to get the vulnerability list of this image.")


  public String getDetailsKey() {
    return detailsKey;
  }

  public void setDetailsKey(String detailsKey) {
    this.detailsKey = detailsKey;
  }

  public DetailedTagScanOverview components(DetailedTagScanOverviewComponents components) {
    this.components = components;
    return this;
  }

  /**
   * Get components
   * @return components
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DetailedTagScanOverviewComponents getComponents() {
    return components;
  }

  public void setComponents(DetailedTagScanOverviewComponents components) {
    this.components = components;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetailedTagScanOverview detailedTagScanOverview = (DetailedTagScanOverview) o;
    return Objects.equals(this.digest, detailedTagScanOverview.digest) &&
        Objects.equals(this.scanStatus, detailedTagScanOverview.scanStatus) &&
        Objects.equals(this.jobId, detailedTagScanOverview.jobId) &&
        Objects.equals(this.severity, detailedTagScanOverview.severity) &&
        Objects.equals(this.detailsKey, detailedTagScanOverview.detailsKey) &&
        Objects.equals(this.components, detailedTagScanOverview.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(digest, scanStatus, jobId, severity, detailsKey, components);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetailedTagScanOverview {\n");

    sb.append("    digest: ").append(toIndentedString(digest)).append("\n");
    sb.append("    scanStatus: ").append(toIndentedString(scanStatus)).append("\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    detailsKey: ").append(toIndentedString(detailsKey)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
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

