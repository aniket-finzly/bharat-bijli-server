package com.finzly.bbc.constants;

import org.springframework.http.HttpStatus;

public enum Error {
    RESOURCE_NOT_FOUND ("E001", "Resource not found", HttpStatus.NOT_FOUND),
    BAD_REQUEST ("E002", "Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED ("E003", "Unauthorized access", HttpStatus.UNAUTHORIZED),
    CONFLICT ("E004", "Conflict occurred", HttpStatus.CONFLICT),
    VALIDATION_ERROR ("E005", "Validation error", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR ("E006", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),

    // OTP Errors
    OTP_NOT_FOUND ("E007", "OTP not found", HttpStatus.NOT_FOUND),
    OTP_EXPIRED ("E008", "OTP has expired", HttpStatus.BAD_REQUEST),
    OTP_ATTEMPT_LIMIT_EXCEEDED ("E009", "OTP attempt limit exceeded", HttpStatus.FORBIDDEN),
    OTP_GENERATION_FAILED ("E010", "Failed to generate OTP", HttpStatus.INTERNAL_SERVER_ERROR),
    OTP_INVALID ("E011", "Invalid OTP", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    Error (String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode () {
        return code;
    }

    public String getMessage () {
        return message;
    }

    public HttpStatus getHttpStatus () {
        return httpStatus;
    }
}
