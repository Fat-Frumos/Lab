package com.epam.esm.exception;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(final String message) {
        super(message);
    }
}
