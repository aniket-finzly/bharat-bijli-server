package com.finzly.bbc.exceptions.custom.auth;

public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException (String message) {
        super (message);
    }
}
