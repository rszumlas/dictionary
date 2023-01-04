package com.rszumlas.dictionary.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        return new ResponseEntity<>(
                new ApiException(e.getMessage(), e.getStatus(), Instant.now()),
                e.getStatus()
        );
    }

}
