package com.finzly.bbc.exceptions.custom.payment;

public class PaymentHistoryNotFoundException extends RuntimeException {
    public PaymentHistoryNotFoundException (String message) {
        super (message);
    }
}
