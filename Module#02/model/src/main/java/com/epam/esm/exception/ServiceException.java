package com.epam.esm.exception;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ServiceException extends RuntimeException {
    private static final Logger LOGGER = LogManager.getLogger();

    public ServiceException(
            final Throwable e) {
        super(e.getMessage());
        LOGGER.error("{}",
                e.getMessage());
    }

    public ServiceException(
            final String message,
            final Throwable e) {
        super(message, e);
        LOGGER.error("{}{}",
                message, e.getMessage());
    }
}
