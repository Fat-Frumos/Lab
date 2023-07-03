package com.epam.esm.exception;

/**
 * Exception thrown when an unauthorized access attempt is made.
 */
public class UnauthorizedAccessException extends RuntimeException {
    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedAccessException(
            final String message) {
        super(message);
    }
}
