package com.finzly.bbc.exceptions.custom.payment;

public class AccountException extends RuntimeException {
    public AccountException (String message) {
        super (message);
    }
}
