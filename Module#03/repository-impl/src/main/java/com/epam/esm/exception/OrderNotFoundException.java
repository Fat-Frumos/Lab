package com.epam.esm.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(final String message) {
        super(message);
    }
}
