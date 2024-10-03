package com.finzly.bbc.exceptions.global;

import com.finzly.bbc.exceptions.custom.BadRequestException;
import com.finzly.bbc.exceptions.custom.ConflictException;
import com.finzly.bbc.exceptions.custom.ResourceNotFoundException;
import com.finzly.bbc.exceptions.custom.UnauthorizedException;
import com.finzly.bbc.exceptions.custom.otp.*;
import com.finzly.bbc.exceptions.validation.ValidationException;
import com.finzly.bbc.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException (ResourceNotFoundException ex) {
        return ResponseEntity.status (HttpStatus.NOT_FOUND)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.NOT_FOUND.value ()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException (BadRequestException ex) {
        return ResponseEntity.status (HttpStatus.BAD_REQUEST)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.BAD_REQUEST.value ()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException (UnauthorizedException ex) {
        return ResponseEntity.status (HttpStatus.UNAUTHORIZED)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.UNAUTHORIZED.value ()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException (ConflictException ex) {
        return ResponseEntity.status (HttpStatus.CONFLICT)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.CONFLICT.value ()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException (ValidationException ex) {
        Map<String, String> validationErrors = ex.getValidationErrors ();
        return ResponseEntity.status (HttpStatus.BAD_REQUEST)
                .body (ApiResponse.error ("Validation errors: " + validationErrors.toString (), HttpStatus.BAD_REQUEST.value (), validationErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException (Exception ex) {
        return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR)
                .body (ApiResponse.error ("An unexpected error occurred: " + ex.getMessage ()));
    }

    // OTP exceptions
    @ExceptionHandler(OtpNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOtpNotFoundException (OtpNotFoundException ex) {
        return ResponseEntity.status (HttpStatus.NOT_FOUND)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.NOT_FOUND.value ()));
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleOtpExpiredException (OtpExpiredException ex) {
        return ResponseEntity.status (HttpStatus.GONE)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.GONE.value ()));
    }

    @ExceptionHandler(OtpAttemptLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleOtpAttemptLimitExceededException (OtpAttemptLimitExceededException ex) {
        return ResponseEntity.status (HttpStatus.TOO_MANY_REQUESTS)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.TOO_MANY_REQUESTS.value ()));
    }

    @ExceptionHandler(OtpGenerationException.class)
    public ResponseEntity<ApiResponse<Void>> handleOtpGenerationException (OtpGenerationException ex) {
        return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR)
                .body (ApiResponse.error ("Failed to generate OTP: " + ex.getMessage ()));
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidOtpException (InvalidOtpException ex) {
        return ResponseEntity.status (HttpStatus.BAD_REQUEST)
                .body (ApiResponse.error (ex.getMessage (), HttpStatus.BAD_REQUEST.value ()));
    }
}
