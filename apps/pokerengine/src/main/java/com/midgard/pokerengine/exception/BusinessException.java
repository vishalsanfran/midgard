package com.midgard.pokerengine.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for business logic related errors.
 * Includes HTTP status code for appropriate error responses.
 */
public class BusinessException extends RuntimeException {
  private final HttpStatus status;

  public BusinessException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}