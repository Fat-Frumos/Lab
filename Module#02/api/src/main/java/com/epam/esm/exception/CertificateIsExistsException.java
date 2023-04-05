package com.epam.esm.exception;

public class CertificateIsExistsException extends RuntimeException {
    public CertificateIsExistsException(String message) {
        super(message);
    }
}
