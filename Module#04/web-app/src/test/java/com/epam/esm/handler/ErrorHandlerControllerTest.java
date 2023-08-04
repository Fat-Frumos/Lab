package com.epam.esm.handler;

import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerControllerTest {

    ErrorHandlerController exceptionHandler = new ErrorHandlerController();

    @Test
    void conflictShouldReturnConflictResponse() {
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Test message");

        ResponseEntity<Object> response = exceptionHandler.conflict(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    @Test
    void handleNoSuchElementFoundExceptionShouldReturnNotFoundResponse() {
        ResourceNotFoundException exception =
                new ResourceNotFoundException("GET", "/test", new HttpHeaders());
        ResponseEntity<Object> response = exceptionHandler
                .handleNoSuchElementFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}