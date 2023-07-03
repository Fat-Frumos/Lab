package com.epam.esm;

import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.handler.ErrorHandlerController;
import com.epam.esm.handler.ResponseMessage;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;

class ExceptionHandlerTest {
    private final static String[] messages = {
            "Bad request",
            "Element Not Found",
            "Something's not right",
            "Something's not right",
            "Element Not Found"};

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
        ResponseEntity<Object> response = exceptionHandler.handleEntityIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) response.getBody();
        assertEquals(messages[0], Objects.requireNonNull(responseMessage).getErrorMessage());
        assertEquals(BAD_REQUEST, responseMessage.getStatusCode());
    }

    @Test
    @DisplayName("Handle Tag Is Exists Exception")
    void handleTagIsExistsException() {
        TagAlreadyExistsException exception = new TagAlreadyExistsException(messages[0]);
        ResponseEntity<Object> response = exceptionHandler.handleEntityIsExistsException(exception);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) response.getBody();
        assertNotNull(responseMessage);
        assertEquals(messages[0], responseMessage.getErrorMessage());
        assertEquals(BAD_REQUEST, responseMessage.getStatusCode());
    }

    @Test
    @DisplayName("Handle Certificate Not Found Exception")
    void testHandleCertificateNotFoundException() {
        CertificateNotFoundException exception = new CertificateNotFoundException(messages[1]);
        ResponseEntity<Object> response = exceptionHandler.handleEntityNotFoundException(exception);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseMessage.builder().errorMessage(messages[1]).statusCode(NOT_FOUND).build(), response.getBody());
    }

    @Test
    @DisplayName("Test handleException method")
    void testHandleException() {
        Exception exception = new Exception(messages[2]);
        ResponseEntity<Object> response = exceptionHandler.handleException(exception);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) response.getBody();
        assertEquals(INTERNAL_SERVER_ERROR, Objects.requireNonNull(responseMessage).getStatusCode());
        assertEquals(messages[2], responseMessage.getErrorMessage());
    }

    @Test
    @DisplayName("Test handleNotAllowedException method")
    void testHandleNotAllowedException() {
        RuntimeException exception = new RuntimeException(messages[3]);
        ResponseEntity<Object> response = exceptionHandler.handleException(exception);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) response.getBody();
        assertEquals(INTERNAL_SERVER_ERROR, Objects.requireNonNull(responseMessage).getStatusCode());
        assertEquals(messages[3], responseMessage.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle TagNotFoundException and return 404 response")
    void testHandleTagNotFoundException() {
        TagNotFoundException exception = new TagNotFoundException(messages[4]);
        ResponseEntity<Object> response = exceptionHandler.handleEntityNotFoundException(exception);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ResponseMessage responseMessage = (ResponseMessage) response.getBody();
        assertEquals(messages[4], responseMessage.getErrorMessage());
        assertEquals(NOT_FOUND, responseMessage.getStatusCode());
    }
}
