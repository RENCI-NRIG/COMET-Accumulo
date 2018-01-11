/*
 * COMET Accumulo Query Layer API
 * COMET Accumulo Query Layer API
 *
 * OpenAPI spec version: 1.0.0
 * Contact: cwang@renci.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * CometResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-10T21:01:26.756Z")
public class CometResponse   {
  @JsonProperty("status")
  private String status = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("value")
  private Object value = null;

  @JsonProperty("version")
  private String version = null;

  public CometResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * COMET status code
   * @return status
   **/
  @JsonProperty("status")
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
  @JsonProperty("message")
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
  @JsonProperty("value")
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
  @JsonProperty("version")
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

