package com.midgard.pokerengine.model.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void builder_AllFields_CreatesApiError() {
        LocalDateTime timestamp = LocalDateTime.now();
        ApiError error = ApiError.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .path("/api/v1/test")
                .build();

        assertEquals(timestamp, error.getTimestamp());
        assertEquals(400, error.getStatus());
        assertEquals("Bad Request", error.getError());
        assertEquals("Invalid input", error.getMessage());
        assertEquals("/api/v1/test", error.getPath());
    }

    @Test
    void equals_SameValues_ReturnsTrue() {
        LocalDateTime timestamp = LocalDateTime.now();
        ApiError error1 = ApiError.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .path("/api/v1/test")
                .build();

        ApiError error2 = ApiError.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .path("/api/v1/test")
                .build();

        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    void equals_DifferentValues_ReturnsFalse() {
        LocalDateTime timestamp = LocalDateTime.now();
        ApiError error1 = ApiError.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .path("/api/v1/test")
                .build();

        ApiError error2 = ApiError.builder()
                .timestamp(timestamp)
                .status(500)
                .error("Internal Server Error")
                .message("System error")
                .path("/api/v1/test")
                .build();

        assertNotEquals(error1, error2);
        assertNotEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        LocalDateTime timestamp = LocalDateTime.now();
        ApiError error = ApiError.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .path("/api/v1/test")
                .build();

        String toString = error.toString();
        assertTrue(toString.contains("timestamp=" + timestamp));
        assertTrue(toString.contains("status=400"));
        assertTrue(toString.contains("error=Bad Request"));
        assertTrue(toString.contains("message=Invalid input"));
        assertTrue(toString.contains("path=/api/v1/test"));
    }

    @Test
    void builder_NullValues_CreatesApiError() {
        ApiError error = ApiError.builder()
                .timestamp(null)
                .status(0)
                .error(null)
                .message(null)
                .path(null)
                .build();

        assertNull(error.getTimestamp());
        assertEquals(0, error.getStatus());
        assertNull(error.getError());
        assertNull(error.getMessage());
        assertNull(error.getPath());
    }
}