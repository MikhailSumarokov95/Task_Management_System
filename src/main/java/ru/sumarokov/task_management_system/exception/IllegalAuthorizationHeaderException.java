package ru.sumarokov.task_management_system.exception;

public class IllegalAuthorizationHeaderException extends RuntimeException {
    public IllegalAuthorizationHeaderException(String message) {
        super(message);
    }
}
