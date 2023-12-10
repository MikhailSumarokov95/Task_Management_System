package ru.sumarokov.task_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.sumarokov.task_management_system.exception.EntityNotFoundException;
import ru.sumarokov.task_management_system.exception.IllegalAuthorizationHeaderException;

import java.io.IOException;

@ControllerAdvice
public class RestControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);

    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Resource is not found")
    @ExceptionHandler(EntityNotFoundException.class)
    public void handleNotFound(Throwable t) {
        LOGGER.error(t.getMessage(), t);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AccessDeniedException.class,
            MethodArgumentNotValidException.class})
    public void handleBadRequest(Throwable t) {
        LOGGER.error(t.getMessage(), t);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalAuthorizationHeaderException.class)
    public void handleForbidden(Throwable t) {
        LOGGER.error("", t);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public void handleServerError(Throwable t) {
        LOGGER.error("", t);
    }
}
