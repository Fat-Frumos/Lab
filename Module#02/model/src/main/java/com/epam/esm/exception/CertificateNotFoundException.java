package com.epam.esm.exception;

/**
 * Exception thrown when a requested certificate is not found in the system.
 */
public class CertificateNotFoundException extends RuntimeException {

    /**
     * Constructs a new CertificateNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public CertificateNotFoundException(final String message) {
        super(message);
    }
}
