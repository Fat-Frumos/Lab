package com.epam.esm;

import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.handler.ErrorHandlerController;
import com.epam.esm.handler.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class ExceptionHandlerTest {
    private final static String[] messages = {
            "Tag already exists",
            "Certificate not found",
            "Internal Server Error",
            "Method Not Allowed",
            "Tag not found with id 1"};

    @InjectMocks
    private ErrorHandlerController exceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test handle Certificate Is Exists Exception method")
    void testHandleCertificateIsExistsException() {
        CertificateAlreadyExistsException exception = new CertificateAlreadyExistsException(messages[0]);
        ResponseEntity<Object> response = exceptionHandler.handleCertificateIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(messages[0], Objects.requireNonNull(errorResponse).getErrorMessage());
        assertEquals(BAD_REQUEST.value(), errorResponse.getErrorCode());
    }

    @Test
    @DisplayName("Handle Tag Is Exists Exception")
    void handleTagIsExistsException() {
        TagAlreadyExistsException exception = new TagAlreadyExistsException(messages[0]);
        ResponseEntity<Object> response = exceptionHandler.handleTagIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(messages[0], errorResponse.getErrorMessage());
        assertEquals(BAD_REQUEST.value(), errorResponse.getErrorCode());
    }

    @Test
    @DisplayName("Handle Certificate Not Found Exception")
    void testHandleCertificateNotFoundException() {
        CertificateNotFoundException exception = new CertificateNotFoundException(messages[1]);
        ResponseEntity<Object> response = exceptionHandler.handleCertificateNotFoundException(exception);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals(new ErrorResponse(messages[1], 40401), response.getBody());
    }

    @Test
    @DisplayName("Test handleException method")
    void testHandleException() {
        Exception exception = new Exception(messages[2]);
        ResponseEntity<Object> response = exceptionHandler.handleException(exception);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(INTERNAL_SERVER_ERROR.value(), Objects.requireNonNull(errorResponse).getErrorCode());
        assertEquals(messages[2], errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Test handleNotAllowedException method")
    void testHandleNotAllowedException() {
        RuntimeException exception = new RuntimeException(messages[3]);
        ResponseEntity<Object> response = exceptionHandler.handleNotAllowedException(exception);
        assertEquals(METHOD_NOT_ALLOWED, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(METHOD_NOT_ALLOWED.value(), Objects.requireNonNull(errorResponse).getErrorCode());
        assertEquals(messages[3], errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle TagNotFoundException and return 404 response")
    void testHandleTagNotFoundException() {
        TagNotFoundException exception = new TagNotFoundException(messages[4]);
        ResponseEntity<Object> response = exceptionHandler.handleTagNotFoundException(exception);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(messages[4], errorResponse.getErrorMessage());
        assertEquals(40401, errorResponse.getErrorCode());
    }
}
