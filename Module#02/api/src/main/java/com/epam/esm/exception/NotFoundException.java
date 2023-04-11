package com.epam.esm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.NoHandlerFoundException;

public class NotFoundException extends NoHandlerFoundException {
    public NotFoundException(
            final String httpMethod,
            final String url,
            final HttpHeaders httpHeaders) {
        super(httpMethod, url, httpHeaders);
    }
}
