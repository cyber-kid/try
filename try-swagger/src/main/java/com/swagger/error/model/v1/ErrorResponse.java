package com.swagger.error.model.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Error representation")
public class ErrorResponse {
  @ApiModelProperty(
          value = "Error message",
          accessMode = ApiModelProperty.AccessMode.READ_ONLY)
  private String errorMessage;

  @ApiModelProperty(
          value = "Error cause",
          accessMode = ApiModelProperty.AccessMode.READ_ONLY)
  private ErrorResponse cause;
}
