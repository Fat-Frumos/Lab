package com.epam.esm.handler;

import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * This class is a RestControllerAdvice used to handle exceptions
 * and return appropriate HTTP response codes and error messages.
 * <p>
 * It is annotated with {@code @RestControllerAdvice}
 * to indicate that it is used to provide
 * a centralized exception handling for controllers.
 */
@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    private static final int ERROR_CODE = 40401;

    /**
     * Handles CertificateNotFoundException and returns
     * a 404 Not Found HTTP response with a custom error message.
     *
     * @param e CertificateNotFoundException that was thrown
     * @return ResponseEntity with ErrorResponse
     * and 404 Not Found HTTP status code
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(
            final CertificateNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), ERROR_CODE),
                NOT_FOUND);
    }

    /**
     * Handles TagNotFoundException and returns a 404
     * Not Found HTTP response with a custom error message.
     *
     * @param e TagNotFoundException that was thrown
     * @return ResponseEntity with ErrorResponse
     * and 404 Not Found HTTP status code
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Object> handleTagNotFoundException(
            final TagNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), ERROR_CODE),
                NOT_FOUND);
    }

    /**
     * Handles CertificateAlreadyExistsException
     * and returns a 400 Bad Request HTTP response with a custom error message.
     *
     * @param e CertificateAlreadyExistsException that was thrown
     * @return ResponseEntity with ErrorResponse
     * and 400 Bad Request HTTP status code
     */
    @ExceptionHandler(CertificateAlreadyExistsException.class)
    public ResponseEntity<Object> handleCertificateIsExistsException(
            final CertificateAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), BAD_REQUEST.value()),
                BAD_REQUEST);
    }

    /**
     * Handles TagAlreadyExistsException and returns
     * a 400 Bad Request HTTP response with a custom error message.
     *
     * @param e TagAlreadyExistsException that was thrown
     * @return ResponseEntity with ErrorResponse
     * and 400 Bad Request HTTP status code
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<Object> handleTagIsExistsException(
            final TagAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), BAD_REQUEST.value()),
                BAD_REQUEST);
    }

    /**
     * Handles any Exception and returns a 500 Internal Server
     * Error HTTP response with a custom error message.
     *
     * @param e Exception that was thrown
     * @return ResponseEntity with ErrorResponse
     * and 500 Internal Server Error HTTP status code
     */

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR.value()),
                INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles any RuntimeException and returns a 405 Method Not Allowed HTTP
     * Error HTTP response with a custom error message.
     *
     * @param e RuntimeException that was thrown
     * @return ResponseEntity with ErrorResponse
     * and 405 Method Not Allowed HTTP status code
     */

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleNotAllowedException(
            final RuntimeException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), METHOD_NOT_ALLOWED.value()),
                METHOD_NOT_ALLOWED);
    }

    /**
     * A single place to customize the response body of all exception types.
     * <p>The default implementation sets the request attribute
     * and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     *
     * @param ex      the exception
     * @param body    the body for the response
     * @param headers the headers for the response
     * @param status  the response status
     * @param request the current request
     */
    @Override
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse("Bad request", status.value()),
                BAD_REQUEST);
    }
}
