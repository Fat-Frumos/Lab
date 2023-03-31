package com.epam.esm.handler;

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
