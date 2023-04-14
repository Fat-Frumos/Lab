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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class ExceptionHandlerTest {

    @InjectMocks
    private ErrorHandlerController exceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test handle Certificate Is Exists Exception method")
    void testHandleCertificateIsExistsException() {
        CertificateAlreadyExistsException exception = new CertificateAlreadyExistsException("Tag already exists");
        ResponseEntity<Object> response = exceptionHandler.handleCertificateIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Tag already exists", errorResponse.getErrorMessage());
        assertEquals(BAD_REQUEST.value(), errorResponse.getErrorCode());
    }

    @Test
    @DisplayName("Handle Tag Is Exists Exception")
    void handleTagIsExistsException() {
        TagAlreadyExistsException exception = new TagAlreadyExistsException("Tag already exists");
        ResponseEntity<Object> response = exceptionHandler.handleTagIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Tag already exists", errorResponse.getErrorMessage());
        assertEquals(BAD_REQUEST.value(), errorResponse.getErrorCode());
    }

    @Test
    @DisplayName("Handle Certificate Not Found Exception")
    void testHandleCertificateNotFoundException() {
        CertificateNotFoundException exception = new CertificateNotFoundException("Certificate not found");
        ResponseEntity<Object> response = exceptionHandler.handleCertificateNotFoundException(exception);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals(new ErrorResponse("Certificate not found", 40401), response.getBody());
    }

    @Test
    @DisplayName("Test handleException method")
    void testHandleException() {
        String errorMessage = "Internal Server Error";
        Exception e = new Exception(errorMessage);

        ResponseEntity<Object> response = exceptionHandler.handleException(e);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(INTERNAL_SERVER_ERROR.value(), errorResponse.getErrorCode());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Test handleNotAllowedException method")
    void testHandleNotAllowedException() {
        String errorMessage = "Method Not Allowed";
        RuntimeException e = new RuntimeException(errorMessage);
        ResponseEntity<Object> response = exceptionHandler.handleNotAllowedException(e);
        assertEquals(METHOD_NOT_ALLOWED, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(METHOD_NOT_ALLOWED.value(), errorResponse.getErrorCode());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle TagNotFoundException and return 404 response")
    void testHandleTagNotFoundException() {
        TagNotFoundException exception = new TagNotFoundException("Tag not found with id 1");
        ResponseEntity<Object> response = exceptionHandler.handleTagNotFoundException(exception);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Tag not found with id 1", errorResponse.getErrorMessage());
        assertEquals(40401, errorResponse.getErrorCode());
    }
}
