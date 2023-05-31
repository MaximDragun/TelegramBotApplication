package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebHookControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> notFoundException(Exception exception) {
        return ResponseEntity
                .internalServerError()
                .body(exception.getMessage());
    }
}
