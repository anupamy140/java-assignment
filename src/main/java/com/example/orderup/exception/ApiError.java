package com.example.orderup.exception;

import java.time.Instant;

/**
 * Standard structure for API error responses.
 */
public class ApiError {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;

    public ApiError(Instant timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
