package com.epam.esm.controller.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
class CertificateNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger();

    CertificateNotFoundException(final String message) {
        super(message);
        LOGGER.error("An exception: {}", message);
    }
}
