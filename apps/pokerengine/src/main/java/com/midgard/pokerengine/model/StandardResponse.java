package com.midgard.pokerengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StandardResponse<T> {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final T data;
}