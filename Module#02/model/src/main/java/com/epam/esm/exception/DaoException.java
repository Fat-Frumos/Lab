package com.epam.esm.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DaoException extends RuntimeException {
    private static final Logger LOGGER = LogManager.getLogger();

    public DaoException(final Throwable cause) {
        super(cause);
        LOGGER.error(
                "An error occurred: {}",
                cause.getMessage());
    }
}
