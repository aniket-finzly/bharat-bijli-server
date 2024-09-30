package com.finzly.bbc.exceptions.custom.auth;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException (String message) {
        super (message);
    }
}


