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
public class CustomApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private String path;
    private int statusCode;
    private Object errors;
    private long timestamp;

    public CustomApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.path = getCurrentPath();
        this.statusCode = 200;
        this.timestamp = System.currentTimeMillis();
    }

    public CustomApiResponse(String status, String message, int statusCode, Object errors) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.path = getCurrentPath();
        this.statusCode = statusCode;
        this.errors = errors;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> CustomApiResponse<T> error(String message) {
        return error(message, 500, null);
    }

    public static <T> CustomApiResponse<T> error(String message, int statusCode) {
        return error(message, statusCode, null);
    }

    public static <T> CustomApiResponse<T> error(String message, int statusCode, Object errors) {
        return new CustomApiResponse<>("error", message, statusCode, errors);
    }

    public static <T> CustomApiResponse<T> success(String message, T data) {
        return new CustomApiResponse<>("success", message, data);
    }

    private static String getCurrentPath() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getRequestURI();
    }
}
