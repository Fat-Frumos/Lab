package com.epam.esm.exception;

public class CertificateAlreadyExistsException extends RuntimeException {
        public CertificateAlreadyExistsException(final String message) {
        super(message);
    }
}
