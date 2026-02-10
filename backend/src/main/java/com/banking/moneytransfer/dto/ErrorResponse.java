package com.banking.moneytransfer.dto;

import java.time.LocalDateTime;

/**
 * DTO for error response
 */
public record ErrorResponse(LocalDateTime timestamp, int status, String error, String path) {

    public ErrorResponse(int status, String error, String path) {
        this(LocalDateTime.now(), status, error, path);
    }
}