package org.example.assistantonsbservlet.exception;

import jakarta.persistence.EntityNotFoundException;
import org.example.assistantonsbservlet.api.AppErrorResponse;
import org.example.assistantonsbservlet.api.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        final var entityNotFound = ErrorCode.ENTITY_NOT_FOUND;
        final var body = new AppErrorResponse(entityNotFound.getCode());
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(entityNotFound.getHttpStatus()).body(body);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<AppErrorResponse> handleAppException(AppException e) {
        final var errorCode = e.getErrorCode();
        final var body = new AppErrorResponse(errorCode.getCode());
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }
}
