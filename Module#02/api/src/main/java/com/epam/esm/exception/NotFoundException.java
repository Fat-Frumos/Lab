package com.epam.esm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends NoHandlerFoundException {
    public NotFoundException(
            final String httpMethod,
            final String url,
            final HttpHeaders httpHeaders) {
        super(httpMethod, url, httpHeaders);
    }
}
