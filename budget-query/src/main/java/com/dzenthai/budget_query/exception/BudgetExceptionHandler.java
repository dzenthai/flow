package com.dzenthai.budget_query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;


@ControllerAdvice
public class BudgetExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        return buildExceptionData(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return buildExceptionData(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildExceptionData(e, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> buildExceptionData(final Exception exception, final HttpStatusCode status) {
        var data = Map.of(
                "message", exception.getMessage(),
                "code", status.value(),
                "timestamp", Instant.now()
        );
        return new ResponseEntity<>(data, status);
    }
}
