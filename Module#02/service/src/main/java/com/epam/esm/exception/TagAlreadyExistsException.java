package com.epam.esm.exception;

/**
 * Thrown when attempting to create a new tag with a name
 * that already exists in the system.
 */
public class TagAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new {@code TagAlreadyExistsException}
     * with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public TagAlreadyExistsException(
            final String message) {
        super(message);
    }
}
