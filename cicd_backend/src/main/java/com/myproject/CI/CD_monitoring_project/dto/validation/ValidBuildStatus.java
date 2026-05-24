package com.myproject.CI.CD_monitoring_project.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.myproject.CI.CD_monitoring_project.entities.enums.BuildStatus;

/**
 * Custom validator annotation to validate BuildStatus enum values.
 */
@Constraint(validatedBy = ValidBuildStatusValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBuildStatus {
    String message() default "Invalid build status. Must be one of: QUEUED, RUNNING, SUCCESS, FAILED, ABORTED, UNSTABLE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validator implementation for ValidBuildStatus annotation.
 */
class ValidBuildStatusValidator implements ConstraintValidator<ValidBuildStatus, String> {

    @Override
    public void initialize(ValidBuildStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        
        try {
            BuildStatus.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
