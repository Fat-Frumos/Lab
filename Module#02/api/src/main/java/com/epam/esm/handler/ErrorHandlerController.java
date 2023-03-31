package com.epam.esm.handler;

import com.epam.esm.exception.CertificateNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(
            final CertificateNotFoundException e) {
        ErrorResponse errorResponse =
                new ErrorResponse(e.getMessage(), 40401);
        return new ResponseEntity<>(
                errorResponse, NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRuntimeException(
            final RuntimeException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getMessage());
        errorResponse.put("errorCode", 500);
        return new ResponseEntity<>(
                errorResponse,
                INTERNAL_SERVER_ERROR);
    }
}
