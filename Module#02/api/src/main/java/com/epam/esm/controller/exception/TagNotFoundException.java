package com.epam.esm.controller.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TagNotFoundException extends RuntimeException {
    private static final Logger LOGGER = LogManager.getLogger();
    public TagNotFoundException(final Throwable e) {
        super(e);
        LOGGER.error("TagNotFoundException", e);
    }
}
