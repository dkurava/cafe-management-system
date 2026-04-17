package com.minicoy.demo.exception;

import java.time.LocalDateTime;

// this is what gets sent back as JSON!
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    // constructor
    public ErrorResponse(int status,
                         String error,
                         String message) {
        this.status    = status;
        this.error     = error;
        this.message   = message;
        this.timestamp = LocalDateTime.now();
    }

    // getters — needed for Jackson to convert to JSON!
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}