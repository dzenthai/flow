package com.dzenthai.budget_query.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;


@Slf4j
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
        var message = exception.getMessage();
        var code = status.value();
        var timestamp = Instant.now();
        var data = Map.of(
                "message", message,
                "code", status,
                "timestamp", timestamp
        );
        log.warn("BudgetExceptionHandler | message: {} | code: {} | timestamp: {}",
                message, code, timestamp);
        return new ResponseEntity<>(data, status);
    }
}
