package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ProjectMetadata
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T08:16:31.961Z")

public class ProjectMetadata   {
  @JsonProperty("public")
  private String _public = null;

  @JsonProperty("enable_content_trust")
  private String enableContentTrust = null;

  @JsonProperty("prevent_vul")
  private String preventVul = null;

  @JsonProperty("severity")
  private String severity = null;

  @JsonProperty("auto_scan")
  private String autoScan = null;

  public ProjectMetadata _public(String _public) {
    this._public = _public;
    return this;
  }

  /**
   * The public status of the project. The valid values are \"true\", \"false\".
   * @return _public
  **/
  @ApiModelProperty(value = "The public status of the project. The valid values are \"true\", \"false\".")


  public String getPublic() {
    return _public;
  }

  public void setPublic(String _public) {
    this._public = _public;
  }

  public ProjectMetadata enableContentTrust(String enableContentTrust) {
    this.enableContentTrust = enableContentTrust;
    return this;
  }

  /**
   * Whether content trust is enabled or not. If it is enabled, user cann't pull unsigned images from this project. The valid values are \"true\", \"false\".
   * @return enableContentTrust
  **/
  @ApiModelProperty(value = "Whether content trust is enabled or not. If it is enabled, user cann't pull unsigned images from this project. The valid values are \"true\", \"false\".")


  public String getEnableContentTrust() {
    return enableContentTrust;
  }

  public void setEnableContentTrust(String enableContentTrust) {
    this.enableContentTrust = enableContentTrust;
  }

  public ProjectMetadata preventVul(String preventVul) {
    this.preventVul = preventVul;
    return this;
  }

  /**
   * Whether prevent the vulnerable images from running. The valid values are \"true\", \"false\".
   * @return preventVul
  **/
  @ApiModelProperty(value = "Whether prevent the vulnerable images from running. The valid values are \"true\", \"false\".")


  public String getPreventVul() {
    return preventVul;
  }

  public void setPreventVul(String preventVul) {
    this.preventVul = preventVul;
  }

  public ProjectMetadata severity(String severity) {
    this.severity = severity;
    return this;
  }

  /**
   * If the vulnerability is high than severity defined here, the images cann't be pulled. The valid values are \"negligible\", \"low\", \"medium\", \"high\", \"critical\".
   * @return severity
  **/
  @ApiModelProperty(value = "If the vulnerability is high than severity defined here, the images cann't be pulled. The valid values are \"negligible\", \"low\", \"medium\", \"high\", \"critical\".")


  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public ProjectMetadata autoScan(String autoScan) {
    this.autoScan = autoScan;
    return this;
  }

  /**
   * Whether scan images automatically when pushing. The valid values are \"true\", \"false\".
   * @return autoScan
  **/
  @ApiModelProperty(value = "Whether scan images automatically when pushing. The valid values are \"true\", \"false\".")


  public String getAutoScan() {
    return autoScan;
  }

  public void setAutoScan(String autoScan) {
    this.autoScan = autoScan;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectMetadata projectMetadata = (ProjectMetadata) o;
    return Objects.equals(this._public, projectMetadata._public) &&
        Objects.equals(this.enableContentTrust, projectMetadata.enableContentTrust) &&
        Objects.equals(this.preventVul, projectMetadata.preventVul) &&
        Objects.equals(this.severity, projectMetadata.severity) &&
        Objects.equals(this.autoScan, projectMetadata.autoScan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_public, enableContentTrust, preventVul, severity, autoScan);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectMetadata {\n");

    sb.append("    _public: ").append(toIndentedString(_public)).append("\n");
    sb.append("    enableContentTrust: ").append(toIndentedString(enableContentTrust)).append("\n");
    sb.append("    preventVul: ").append(toIndentedString(preventVul)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    autoScan: ").append(toIndentedString(autoScan)).append("\n");
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

