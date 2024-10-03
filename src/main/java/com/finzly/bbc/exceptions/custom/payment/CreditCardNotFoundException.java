package com.finzly.bbc.exceptions.custom.payment;

public class CreditCardNotFoundException extends RuntimeException {

    public CreditCardNotFoundException (String message) {
        super (message);
    }
}


