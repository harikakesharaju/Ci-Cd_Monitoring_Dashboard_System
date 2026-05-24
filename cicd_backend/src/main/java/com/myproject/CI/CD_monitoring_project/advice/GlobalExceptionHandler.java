package com.myproject.CI.CD_monitoring_project.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.myproject.CI.CD_monitoring_project.exception.AppException;
import com.myproject.CI.CD_monitoring_project.exception.ForbiddenException;
import com.myproject.CI.CD_monitoring_project.exception.ResourceNotFoundException;
import com.myproject.CI.CD_monitoring_project.exception.UnauthorizedException;
import com.myproject.CI.CD_monitoring_project.exception.ValidationException;

/**
 * Global exception handler for the CI/CD Monitoring application.
 * Handles all exceptions and returns consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle AppException and its subclasses.
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
        return buildErrorResponse(
            ex.getHttpStatus(),
            ex.getErrorCode(),
            ex.getMessage()
        );
    }

    /**
     * Handle ResourceNotFoundException.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorBody("RESOURCE_NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handle ValidationException.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody("VALIDATION_ERROR", ex.getMessage()));
    }

    /**
     * Handle UnauthorizedException.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorBody("UNAUTHORIZED", ex.getMessage()));
    }

    /**
     * Handle ForbiddenException.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildErrorBody("FORBIDDEN", ex.getMessage()));
    }

    /**
     * Handle AccessDeniedException from Spring Security.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildErrorBody("ACCESS_DENIED", "Access denied: " + ex.getMessage()));
    }

    /**
     * Handle validation errors from @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errorCode", "VALIDATION_ERROR");
        body.put("message", "Validation failed");
        body.put("fieldErrors", fieldErrors);
        
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handle generic RuntimeException.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorBody("INTERNAL_ERROR", 
                    ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred"));
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorBody("INTERNAL_ERROR", 
                    ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred"));
    }

    /**
     * Build standard error response.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorCode, String message) {
        return ResponseEntity.status(status).body(buildErrorBody(errorCode, message));
    }

    /**
     * Build error response body.
     */
    private Map<String, Object> buildErrorBody(String errorCode, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errorCode", errorCode);
        body.put("message", message);
        return body;
    }
}
