package com.finzly.bbc.exceptions.custom;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException (String message) {
        super (message);
    }
}