package com.finzly.bbc.exceptions.custom.auth;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException (String message) {
        super (message);
    }
}


