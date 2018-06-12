package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * CometResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-29T17:51:04.480Z")
public class CometResponse {
  @SerializedName("status")
  private String status = null;

  @SerializedName("message")
  private String message = null;

  @SerializedName("value")
  private Object value = null;

  @SerializedName("version")
  private String version = null;

  public CometResponse status(String status) {
    this.status = status;
    return this;
  }

   /**
   * COMET status code
   * @return status
  **/
  @ApiModelProperty(value = "COMET status code")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public CometResponse message(String message) {
    this.message = message;
    return this;
  }

   /**
   * COMET status message
   * @return message
  **/
  @ApiModelProperty(value = "COMET status message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public CometResponse value(Object value) {
    this.value = value;
    return this;
  }

   /**
   * JSON object
   * @return value
  **/
  @ApiModelProperty(value = "JSON object")
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public CometResponse version(String version) {
    this.version = version;
    return this;
  }

   /**
   * COMET version
   * @return version
  **/
  @ApiModelProperty(value = "COMET version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CometResponse cometResponse = (CometResponse) o;
    return Objects.equals(this.status, cometResponse.status) &&
        Objects.equals(this.message, cometResponse.message) &&
        Objects.equals(this.value, cometResponse.value) &&
        Objects.equals(this.version, cometResponse.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, message, value, version);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CometResponse {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

