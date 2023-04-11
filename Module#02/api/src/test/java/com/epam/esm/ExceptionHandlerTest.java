package com.epam.esm;

import com.epam.esm.exception.CertificateIsExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.TagIsExistsException;
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
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class ExceptionHandlerTest {

    @Mock
    private NotFoundException notFoundException;

    @InjectMocks
    private ErrorHandlerController exceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test handle Not Found Exception")
    void testHandleNotFoundException() {
        when(notFoundException.getMessage()).thenReturn("Requested resource not found");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleNotFoundException(notFoundException);
        ErrorResponse expectedErrorResponse = new ErrorResponse("Requested resource not found", 40401);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(expectedErrorResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test handle Certificate Is Exists Exception method")
    void testHandleCertificateIsExistsException() {
        CertificateIsExistsException exception = new CertificateIsExistsException("Tag already exists");
        ResponseEntity<Object> response = exceptionHandler.handleCertificateIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Tag already exists", errorResponse.getErrorMessage());
        assertEquals(BAD_REQUEST.value(), errorResponse.getErrorCode());
    }

    @Test
    @DisplayName("Handle Tag Is Exists Exception")
    void handleTagIsExistsException() {
        TagIsExistsException exception = new TagIsExistsException("Tag already exists");
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
}
