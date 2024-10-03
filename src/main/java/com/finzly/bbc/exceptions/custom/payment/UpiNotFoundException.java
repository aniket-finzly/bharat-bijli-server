package com.finzly.bbc.exceptions.custom.payment;

public class UpiNotFoundException extends RuntimeException {
    public UpiNotFoundException (String message) {
        super (message);
    }
}


