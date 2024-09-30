package com.finzly.bbc.exceptions.validation;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {
    private final Map<String, String> validationErrors;

    public ValidationException (String message) {
        super (message);
        this.validationErrors = null;
    }

    public ValidationException (Map<String, String> validationErrors) {
        super ("Validation failed");
        this.validationErrors = validationErrors;
    }

}
