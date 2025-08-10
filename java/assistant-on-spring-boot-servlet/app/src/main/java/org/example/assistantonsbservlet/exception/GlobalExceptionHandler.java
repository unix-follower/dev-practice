package org.example.assistantonsbservlet.exception;

import jakarta.persistence.EntityNotFoundException;
import org.example.assistantonsbservlet.api.AppErrorResponse;
import org.example.assistantonsbservlet.api.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        final var body = new AppErrorResponse(ErrorCode.ENTITY_NOT_FOUND.getCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
