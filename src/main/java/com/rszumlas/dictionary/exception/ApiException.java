package com.rszumlas.dictionary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final Instant timestamp;

}
