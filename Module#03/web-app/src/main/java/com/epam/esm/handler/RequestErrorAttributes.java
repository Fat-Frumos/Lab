package com.epam.esm.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class RequestErrorAttributes implements ErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            final WebRequest webRequest,
            final ErrorAttributeOptions options) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        Map<String, Object> errorAttributes = new DefaultErrorAttributes().getErrorAttributes(webRequest, options);
        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("status", INTERNAL_SERVER_ERROR.value());
        errorAttributes.put("error", INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorAttributes.put("message", "An error occurred while processing your request");
        errorAttributes.put("path", webRequest.getDescription(false).replace("uri=", ""));
        errorAttributes.put("method", request.getMethod());
        errorAttributes.put("headers", request.getHeaderNames());
        return errorAttributes;
    }

    @Override
    public Throwable getError(final WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        return (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
}
