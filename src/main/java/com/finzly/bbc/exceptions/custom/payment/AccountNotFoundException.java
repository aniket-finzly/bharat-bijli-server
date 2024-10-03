package com.finzly.bbc.exceptions.custom.payment;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException (String message) {
        super (message);
    }
}


