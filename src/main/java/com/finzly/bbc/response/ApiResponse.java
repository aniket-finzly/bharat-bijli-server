package com.finzly.bbc.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private String path;
    private int statusCode;
    private Object errors;

    public ApiResponse (String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.path = null;
        this.statusCode = 200;
    }

    public ApiResponse (String status, String message, int statusCode, Object errors) {
        this.status = status;
        this.message = message;
        this.data = null; // Will be excluded if null
        this.path = getCurrentPath (); // Will be included if not null
        this.statusCode = statusCode;
        this.errors = errors; // Will be excluded if null
    }

    public static <T> ApiResponse<T> error (String message) {
        return error (message, 500, null); // Default to INTERNAL_SERVER_ERROR
    }

    public static <T> ApiResponse<T> error (String message, int statusCode) {
        return error (message, statusCode, null); // Default to no additional errors
    }

    public static <T> ApiResponse<T> error (String message, int statusCode, Object errors) {
        return new ApiResponse<> ("error", message, statusCode, errors);
    }

    public static <T> ApiResponse<T> success (String message, T data) {
        return new ApiResponse<> ("success", message, data);
    }

    private static String getCurrentPath () {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull (RequestContextHolder.getRequestAttributes ())).getRequest ();
        return request.getRequestURI ();
    }
}
