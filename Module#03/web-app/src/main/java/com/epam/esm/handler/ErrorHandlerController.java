package com.epam.esm.handler;

import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = CONFLICT, reason = "Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> conflict(
            final DataIntegrityViolationException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                CONFLICT
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNoSuchElementFoundException(
            final ResourceNotFoundException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                NOT_FOUND
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({CertificateNotFoundException.class,
            TagNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(
            final RuntimeException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                NOT_FOUND
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({CertificateAlreadyExistsException.class,
            TagAlreadyExistsException.class})
    public ResponseEntity<Object> handleEntityIsExistsException(
            final RuntimeException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                BAD_REQUEST
        );
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, IllegalStateException.class})
    public ResponseEntity<Object> handleException(
            final Exception exception) {
        return buildErrorResponse(
                exception.getMessage(),
                INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            final RuntimeException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                METHOD_NOT_ALLOWED
        );
    }

    private ResponseEntity<Object> buildErrorResponse(
            final String messageError,
            final HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(ResponseMessage.builder()
                        .errorMessage(messageError)
                        .statusCode(httpStatus)
                        .build());
    }
}
