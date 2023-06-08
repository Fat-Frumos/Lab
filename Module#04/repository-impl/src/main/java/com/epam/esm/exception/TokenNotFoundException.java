package com.epam.esm.exception;

/**
 * Exception thrown when a token is not found.
 */
public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(final String message) {
        super(message);
    }
}
