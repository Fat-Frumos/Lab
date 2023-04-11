package com.epam.esm.exception;

public class TagIsExistsException extends RuntimeException {
    public TagIsExistsException(
            final String message) {
        super(message);
    }
}
