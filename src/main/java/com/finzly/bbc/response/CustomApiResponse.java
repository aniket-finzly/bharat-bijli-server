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
    private String completeUrl;
    private int statusCode;
    private Object errors;
    private long timestamp;

    public CustomApiResponse (String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.path = getCurrentPath ();
        this.completeUrl = getCompleteUrl ();
        this.statusCode = 200;
        this.timestamp = System.currentTimeMillis ();
    }

    public CustomApiResponse (String status, String message, int statusCode, Object errors) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.path = getCurrentPath ();
        this.completeUrl = getCompleteUrl ();
        this.statusCode = statusCode;
        this.errors = errors;
        this.timestamp = System.currentTimeMillis ();
    }

    public static <T> CustomApiResponse<T> error (String message) {
        return error (message, 500, null);
    }

    public static <T> CustomApiResponse<T> error (String message, int statusCode) {
        return error (message, statusCode, null);
    }

    public static <T> CustomApiResponse<T> error (String message, int statusCode, Object errors) {
        return new CustomApiResponse<> ("error", message, statusCode, errors);
    }

    public static <T> CustomApiResponse<T> success (String message, T data, int statusCode) {
        CustomApiResponse<T> response = new CustomApiResponse<> ("success", message, data);
        response.setStatusCode (statusCode); // Set custom status code
        return response;
    }

    public static <T> CustomApiResponse<T> success (String message, int statusCode) {
        CustomApiResponse<T> response = new CustomApiResponse<> ("success", message, null);
        response.setStatusCode (statusCode); // Set custom status code
        return response;
    }

    public static <T> CustomApiResponse<T> success (T data, int statusCode) {
        CustomApiResponse<T> response = new CustomApiResponse<> ("success", "Request was successful.", data);
        response.setStatusCode (statusCode); // Set custom status code
        return response;
    }

    public static <T> CustomApiResponse<T> success (int statusCode) {
        CustomApiResponse<T> response = new CustomApiResponse<> ("success", "Request was successful.", null);
        response.setStatusCode (statusCode); // Set custom status code
        return response;
    }

    private static String getCurrentPath () {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull (RequestContextHolder.getRequestAttributes ())).getRequest ();
        return request.getRequestURI ();
    }

    private static String getCurrentUrl () {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull (RequestContextHolder.getRequestAttributes ())).getRequest ();

        String scheme = request.getScheme ();
        String serverName = request.getServerName ();
        int serverPort = request.getServerPort ();
        String contextPath = request.getContextPath ();
        String requestUri = request.getRequestURI ();

        StringBuilder url = new StringBuilder ();
        url.append (scheme).append ("://").append (serverName);

        if ((scheme.equals ("http") && serverPort != 80) || (scheme.equals ("https") && serverPort != 443)) {
            url.append (":").append (serverPort);
        }

        url.append (contextPath).append (requestUri);

        return url.toString ();
    }
}
