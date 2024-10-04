package com.finzly.bbc.exceptions;

import com.finzly.bbc.response.CustomApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //  400 Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    //  401 Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED
        );
    }

    //  402 Payment Required
    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<CustomApiResponse<Object>> handlePaymentRequired(PaymentRequiredException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.PAYMENT_REQUIRED.value()),
                HttpStatus.PAYMENT_REQUIRED
        );
    }

    //  403 Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleForbidden(ForbiddenException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN
        );
    }

    //  404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    //  405 Method Not Allowed
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleMethodNotAllowed(MethodNotAllowedException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value()),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    //  409 Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleConflict(ConflictException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT
        );
    }

    //  415 Unsupported Media Type
    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleUnsupportedMediaType(UnsupportedMediaTypeException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    //  422 Unprocessable Entity
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleUnprocessableEntity(UnprocessableEntityException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    //  429 Too Many Requests
    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleTooManyRequests(TooManyRequestsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value()),
                HttpStatus.TOO_MANY_REQUESTS
        );
    }


    //  500 Internal Server Error
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleInternalServerError(InternalServerErrorException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    //  503 Service Unavailable
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleServiceUnavailable(ServiceUnavailableException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value()),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    //  504 Gateway Timeout
    @ExceptionHandler(GatewayTimeoutException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleGatewayTimeout(GatewayTimeoutException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.GATEWAY_TIMEOUT.value()),
                HttpStatus.GATEWAY_TIMEOUT
        );
    }

    //  500 Internal Server Error
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleDatabaseException(DatabaseException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                CustomApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    //  404 Not Found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleNoHandlerFound(NoHandlerFoundException ex) {
        log.error("No handler found for this request");
        return new ResponseEntity<>(
                CustomApiResponse.error("No handler found for this request", HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }
}
