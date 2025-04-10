package com.midgard.pokerengine.model.error;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * Error response object for API errors.
 * Contains detailed information about the error that occurred.
 */
@Data
@Builder
public class ApiError {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
}