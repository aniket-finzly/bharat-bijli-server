package com.finzly.bbc.exceptions;
// Client-Side (4xx) Exceptions

// Server-Side (5xx) Exceptions

public class DatabaseException extends RuntimeException {
    public DatabaseException (String message) {
        super (message);
    }

    public DatabaseException (String message, Throwable cause) {
        super (message, cause);
    }
}
