package com.epam.esm.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public
class ErrorResponse {
    private String errorMessage;
    private int errorCode;
}
