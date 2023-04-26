package com.epam.esm.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

        private String errorMessage;
        private int errorCode;
}
