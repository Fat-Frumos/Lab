package com.epam.esm.exception;

/**
 * Exception indicating that a certificate
 * with the same name already exists in the system.
 */
public class CertificateAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new CertificateAlreadyExistsException
     * with the specified error message.
     *
     * @param message the error message
     */
    public CertificateAlreadyExistsException(final String message) {
        super(message);
    }
}
