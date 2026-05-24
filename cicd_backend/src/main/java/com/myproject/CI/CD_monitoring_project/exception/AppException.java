package com.myproject.CI.CD_monitoring_project.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception class for the CI/CD Monitoring application.
 * All custom exceptions should extend this class.
 */
public class AppException extends RuntimeException {

    private HttpStatus httpStatus;
    private String errorCode;

    public AppException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = "INTERNAL_ERROR";
    }

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = httpStatus.name();
    }

    public AppException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = "INTERNAL_ERROR";
    }

    public AppException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
