package com.epam.esm.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseMessage {

        private String errorMessage;
        private int statusCode;
}
