package com.myproject.CI.CD_monitoring_project.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user is unauthorized to perform an operation.
 */
public class UnauthorizedException extends AppException {

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException() {
        super("User is not authorized to perform this action", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
}
