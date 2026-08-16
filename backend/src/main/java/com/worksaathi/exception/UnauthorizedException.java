package com.worksaathi.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String resourceName, Long id) {
        super(String.format("Unauthorized access to %s with id: %d", resourceName, id));
    }
}
