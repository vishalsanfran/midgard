package com.midgard.pokerengine.exception;

import com.midgard.pokerengine.model.StandardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardResponse<Void>> handleBusinessException(BusinessException ex) {
        StandardResponse<Void> response = new StandardResponse<>(
            LocalDateTime.now(),
            ex.getStatus().value(),
            ex.getMessage(),
            null
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}