package com.epam.esm.handler;

import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    private static final int ERROR_CODE = 40401;

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(
            final CertificateNotFoundException e) {
        return new ResponseEntity<>(
                new ResponseMessage(e.getMessage(), ERROR_CODE),
                NOT_FOUND);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Object> handleTagNotFoundException(
            final TagNotFoundException e) {
        return new ResponseEntity<>(
                new ResponseMessage(e.getMessage(), ERROR_CODE),
                NOT_FOUND);
    }

    @ExceptionHandler(CertificateAlreadyExistsException.class)
    public ResponseEntity<Object> handleCertificateIsExistsException(
            final CertificateAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ResponseMessage(e.getMessage(), BAD_REQUEST.value()),
                BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<Object> handleTagIsExistsException(
            final TagAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ResponseMessage(e.getMessage(), BAD_REQUEST.value()),
                BAD_REQUEST);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception e) {
        return new ResponseEntity<>(
                new ResponseMessage(e.getMessage(), INTERNAL_SERVER_ERROR.value()),
                INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleNotAllowedException(
            final RuntimeException e) {
        return new ResponseEntity<>(
                new ResponseMessage(e.getMessage(), METHOD_NOT_ALLOWED.value()),
                METHOD_NOT_ALLOWED);
    }
}