package com.epam.esm.exception;

public class CertificateNotFoundException extends RuntimeException {

    public CertificateNotFoundException(final String message) {
        super(message);
    }

    public CertificateNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
