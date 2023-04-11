package com.epam.esm.exception;

public class CertificateIsExistsException extends RuntimeException {
    public CertificateIsExistsException(final String message) {
        super(message);
    }
}
