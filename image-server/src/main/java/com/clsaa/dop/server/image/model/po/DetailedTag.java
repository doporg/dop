package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DetailedTag
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-03-23T08:16:31.961Z")

public class DetailedTag {
  @JsonProperty("digest")
  private String digest = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("size")
  private Integer size = null;

  @JsonProperty("architecture")
  private String architecture = null;

  @JsonProperty("os")
  private String os = null;

  @JsonProperty("docker_version")
  private String dockerVersion = null;

  @JsonProperty("author")
  private String author = null;

  @JsonProperty("created")
  private String created = null;

  @JsonProperty("signature")
  private Object signature = null;

  @JsonProperty("scan_overview")
  private DetailedTagScanOverview scanOverview = null;

  @JsonProperty("labels")
  @Valid
  private List<Label> labels = null;

  public DetailedTag digest(String digest) {
    this.digest = digest;
    return this;
  }

  /**
   * The digest of the tag.
   * @return digest
  **/
  @ApiModelProperty(value = "The digest of the tag.")


  public String getDigest() {
    return digest;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public DetailedTag name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the tag.
   * @return name
  **/
  @ApiModelProperty(value = "The name of the tag.")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DetailedTag size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * The size of the image.
   * @return size
  **/
  @ApiModelProperty(value = "The size of the image.")


  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public DetailedTag architecture(String architecture) {
    this.architecture = architecture;
    return this;
  }

  /**
   * The architecture of the image.
   * @return architecture
  **/
  @ApiModelProperty(value = "The architecture of the image.")


  public String getArchitecture() {
    return architecture;
  }

  public void setArchitecture(String architecture) {
    this.architecture = architecture;
  }

  public DetailedTag os(String os) {
    this.os = os;
    return this;
  }

  /**
   * The os of the image.
   * @return os
  **/
  @ApiModelProperty(value = "The os of the image.")


  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public DetailedTag dockerVersion(String dockerVersion) {
    this.dockerVersion = dockerVersion;
    return this;
  }

  /**
   * The version of docker which builds the image.
   * @return dockerVersion
  **/
  @ApiModelProperty(value = "The version of docker which builds the image.")


  public String getDockerVersion() {
    return dockerVersion;
  }

  public void setDockerVersion(String dockerVersion) {
    this.dockerVersion = dockerVersion;
  }

  public DetailedTag author(String author) {
    this.author = author;
    return this;
  }

  /**
   * The author of the image.
   * @return author
  **/
  @ApiModelProperty(value = "The author of the image.")


  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public DetailedTag created(String created) {
    this.created = created;
    return this;
  }

  /**
   * The build time of the image.
   * @return created
  **/
  @ApiModelProperty(value = "The build time of the image.")


  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public DetailedTag signature(Object signature) {
    this.signature = signature;
    return this;
  }

  /**
   * The signature of image, defined by RepoSignature. If it is null, the image is unsigned.
   * @return signature
  **/
  @ApiModelProperty(value = "The signature of image, defined by RepoSignature. If it is null, the image is unsigned.")


  public Object getSignature() {
    return signature;
  }

  public void setSignature(Object signature) {
    this.signature = signature;
  }

  public DetailedTag scanOverview(DetailedTagScanOverview scanOverview) {
    this.scanOverview = scanOverview;
    return this;
  }

  /**
   * Get scanOverview
   * @return scanOverview
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DetailedTagScanOverview getScanOverview() {
    return scanOverview;
  }

  public void setScanOverview(DetailedTagScanOverview scanOverview) {
    this.scanOverview = scanOverview;
  }

  public DetailedTag labels(List<Label> labels) {
    this.labels = labels;
    return this;
  }

  public DetailedTag addLabelsItem(Label labelsItem) {
    if (this.labels == null) {
      this.labels = new ArrayList<Label>();
    }
    this.labels.add(labelsItem);
    return this;
  }

  /**
   * The label list.
   * @return labels
  **/
  @ApiModelProperty(value = "The label list.")

  @Valid

  public List<Label> getLabels() {
    return labels;
  }

  public void setLabels(List<Label> labels) {
    this.labels = labels;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetailedTag detailedTag = (DetailedTag) o;
    return Objects.equals(this.digest, detailedTag.digest) &&
        Objects.equals(this.name, detailedTag.name) &&
        Objects.equals(this.size, detailedTag.size) &&
        Objects.equals(this.architecture, detailedTag.architecture) &&
        Objects.equals(this.os, detailedTag.os) &&
        Objects.equals(this.dockerVersion, detailedTag.dockerVersion) &&
        Objects.equals(this.author, detailedTag.author) &&
        Objects.equals(this.created, detailedTag.created) &&
        Objects.equals(this.signature, detailedTag.signature) &&
        Objects.equals(this.scanOverview, detailedTag.scanOverview) &&
        Objects.equals(this.labels, detailedTag.labels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(digest, name, size, architecture, os, dockerVersion, author, created, signature, scanOverview, labels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetailedTag {\n");

    sb.append("    digest: ").append(toIndentedString(digest)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    architecture: ").append(toIndentedString(architecture)).append("\n");
    sb.append("    os: ").append(toIndentedString(os)).append("\n");
    sb.append("    dockerVersion: ").append(toIndentedString(dockerVersion)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    scanOverview: ").append(toIndentedString(scanOverview)).append("\n");
    sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
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

