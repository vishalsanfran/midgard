package com.midgard.pokerengine.exception;

import com.midgard.pokerengine.model.error.ApiError;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the application.
 * Handles both business and generic exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles business exceptions thrown by the application.
   *
   * @param ex the business exception
   * @param request the web request
   * @return ResponseEntity containing ApiError
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusinessException(
      BusinessException ex, 
      WebRequest request) {
    ApiError apiError = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(ex.getStatus().value())
        .error(ex.getStatus().getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();
    return new ResponseEntity<>(apiError, ex.getStatus());
  }

  /**
   * Handles all unhandled exceptions.
   *
   * @param ex the exception
   * @param request the web request
   * @return ResponseEntity containing ApiError
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleException(
      Exception ex, 
      WebRequest request) {
    ApiError apiError = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}