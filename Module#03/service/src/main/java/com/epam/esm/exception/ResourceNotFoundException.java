package com.epam.esm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class ResourceNotFoundException extends NoHandlerFoundException {
    public ResourceNotFoundException(
            final String httpMethod,
            final String url,
            final HttpHeaders httpHeaders) {
        super(httpMethod, url, httpHeaders);
    }
}
