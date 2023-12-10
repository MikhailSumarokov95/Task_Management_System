package ru.sumarokov.task_management_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.sumarokov.task_management_system.exception.EntityNotFoundException;
import ru.sumarokov.task_management_system.exception.IllegalAuthorizationHeaderException;

import java.io.IOException;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class,
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalAuthorizationHeaderException.class})
    protected ResponseEntity<Object> handleForbidden(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({IOException.class})
    protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
