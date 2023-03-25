package com.epam.esm.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public
class ErrorResponse {
    private String errorMessage;
    private int errorCode;
}
