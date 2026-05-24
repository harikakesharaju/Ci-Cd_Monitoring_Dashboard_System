package com.myproject.CI.CD_monitoring_project.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user has forbidden access to a resource.
 */
public class ForbiddenException extends AppException {

    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", HttpStatus.FORBIDDEN);
    }

    public ForbiddenException() {
        super("Access to this resource is forbidden", "FORBIDDEN", HttpStatus.FORBIDDEN);
    }
}
