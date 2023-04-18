package com.epam.esm.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The {@code ErrorResponse} class represents the error response returned by the API endpoints.
 * <p>
 * It is used to return error messages and error codes to the client in case of an error in the API.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The field represents a message describing the error that occurred.
     */
    private String errorMessage;
    /**
     * The error code associated with the issue.
     */
    private int errorCode;
}
