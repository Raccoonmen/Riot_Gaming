package com.riot_gaming.model;

import com.riot_gaming.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
  private ErrorCode errorCode;
  private String errorMessage;
  private int httpStatusCode;
}
