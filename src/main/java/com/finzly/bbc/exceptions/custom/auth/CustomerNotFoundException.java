package com.finzly.bbc.exceptions.custom.auth;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException (String message) {
        super (message);
    }
}


