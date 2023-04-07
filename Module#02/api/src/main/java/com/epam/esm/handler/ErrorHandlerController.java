package com.epam.esm.handler;

import com.epam.esm.exception.CertificateIsExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(
            final CertificateNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), 40401),
                NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Requested resource not found", 40401),
                NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(CertificateIsExistsException.class)
    public ResponseEntity<Object> handleCertificateIsExistsException(
            final CertificateNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), BAD_REQUEST.value()),
                BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(
            final Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR.value()),
                INTERNAL_SERVER_ERROR);
    }
    @ResponseBody
    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleNotAllowedException(
            final RuntimeException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), METHOD_NOT_ALLOWED.value()),
                METHOD_NOT_ALLOWED);
    }
}
