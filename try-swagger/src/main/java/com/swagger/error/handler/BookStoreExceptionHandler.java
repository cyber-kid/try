package com.swagger.error.handler;

import com.swagger.error.model.v1.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class BookStoreExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {

    ErrorResponse cause = e.getCause() == null ? null : ErrorResponse.builder().errorMessage(e.getCause().getMessage()).build();

    ErrorResponse errorResponse = ErrorResponse.builder()
            .errorMessage(e.getMessage())
            .cause(cause)
            .build();

    if (e instanceof NoSuchElementException) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

}
