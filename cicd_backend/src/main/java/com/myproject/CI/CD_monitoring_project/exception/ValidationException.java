package com.myproject.CI.CD_monitoring_project.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when validation fails for input data.
 */
public class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public ValidationException(String fieldName, String message) {
        super(
            String.format("Validation failed for field '%s': %s", fieldName, message),
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST
        );
    }
}
