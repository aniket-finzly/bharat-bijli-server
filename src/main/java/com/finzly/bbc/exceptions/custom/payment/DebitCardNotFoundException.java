package com.finzly.bbc.exceptions.custom.payment;

public class DebitCardNotFoundException extends RuntimeException {
    public DebitCardNotFoundException (String message) {
        super (message);
    }
}


