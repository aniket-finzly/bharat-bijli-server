package com.finzly.bbc.exceptions.global;

import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.exceptions.custom.*;
import com.finzly.bbc.exceptions.custom.otp.*;
import com.finzly.bbc.exceptions.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(BadRequestException.class)
    public ApiResponse<Void> handleBadRequestException(BadRequestException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse<Void> handleUnauthorizedException(UnauthorizedException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(ConflictException.class)
    public ApiResponse<Void> handleConflictException(ConflictException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> handleValidationException(ValidationException ex) {
        Map<String, String> validationErrors = ex.getValidationErrors();
        return ApiResponse.error("Validation errors: " + validationErrors.toString(), HttpStatus.BAD_REQUEST.value(), validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(Exception ex) {
        return ApiResponse.error("An unexpected error occurred: " + ex.getMessage());
    }

    // OTP exceptions
    @ExceptionHandler(OtpNotFoundException.class)
    public ApiResponse<Void> handleOtpNotFoundException(OtpNotFoundException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ApiResponse<Void> handleOtpExpiredException(OtpExpiredException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.GONE.value());
    }

    @ExceptionHandler(OtpAttemptLimitExceededException.class)
    public ApiResponse<Void> handleOtpAttemptLimitExceededException(OtpAttemptLimitExceededException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value());
    }

    @ExceptionHandler(OtpGenerationException.class)
    public ApiResponse<Void> handleOtpGenerationException(OtpGenerationException ex) {
        return ApiResponse.error("Failed to generate OTP: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ApiResponse<Void> handleInvalidOtpException(InvalidOtpException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}
