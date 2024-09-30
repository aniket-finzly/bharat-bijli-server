package com.finzly.bbc.exceptions.custom.auth;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super (message);
    }
}


