package com.finzly.bbc.exceptions;

public class GatewayTimeoutException extends RuntimeException {
    public GatewayTimeoutException (String message) {
        super (message);
    }

    public GatewayTimeoutException (String message, Throwable cause) {
        super (message, cause);
    }
}
