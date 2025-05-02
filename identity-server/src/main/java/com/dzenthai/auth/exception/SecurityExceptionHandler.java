package com.dzenthai.auth.exception;

import com.dzenthai.auth.model.dto.ExceptionData;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;


@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        return buildExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return buildExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        return buildExceptionResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return buildExceptionResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<?> handleDisableException(DisabledException ex) {
        return buildExceptionResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return buildExceptionResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(BadCredentialsException ex) {
        return buildExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> buildExceptionResponse(final Exception ex, final HttpStatus status) {
        var timestamp = Instant.now();
        var statusCode = status.value();
        var exception = ex.fillInStackTrace();
        var data = ExceptionData.builder()
                .timestamp(timestamp)
                .status(statusCode)
                .message(exception.getMessage())
                .build();
        log.warn("ExceptionHandler | exception: {}, status: {}, timestamp: {}",
                exception, statusCode, timestamp);
        return new ResponseEntity<>(data, status);
    }
}
