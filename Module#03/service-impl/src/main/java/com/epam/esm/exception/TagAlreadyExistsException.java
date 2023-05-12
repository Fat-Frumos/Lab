package com.epam.esm.exception;

public class TagAlreadyExistsException extends RuntimeException {
        public TagAlreadyExistsException(
            final String message) {
        super(message);
    }
}
