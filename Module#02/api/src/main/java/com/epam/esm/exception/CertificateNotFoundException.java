package com.epam.esm.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class CertificateNotFoundException extends RuntimeException {

    public CertificateNotFoundException(final String message) {
        super(message);
    }
}
