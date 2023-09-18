package com.riot_gaming.exception;

import com.riot_gaming.type.ErrorCode;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewException extends RuntimeException {
  private ErrorCode errorCode;
  private String errorMessage;
  private HttpStatus httpStatus;

  public NewException(ErrorCode errorCode,
      HttpStatus httpStatus) {
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();
    this.httpStatus = httpStatus;
  }
}

