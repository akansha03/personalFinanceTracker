package com.finance.personalTracker.exception;

import com.finance.personalTracker.dto.ErrorResponseDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return buildErrorResponse(
                "Validation Failed",
                "Invalid input provided",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    // Handle business rules error
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessValidation(BusinessValidationException ex) {
        return buildErrorResponse(
                "Business Rule Violation",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                null
        );
    }

    // Handles duplicate resource errors
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResource(DuplicateResourceException ex) {
        return buildErrorResponse(
                "Resource Already Exists",
                ex.getMessage(),
                HttpStatus.CONFLICT,
                null
        );
    }

    // Catch all the unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return buildErrorResponse(
                "Internal Server Error",
                "Something went wrong. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }


    // Method to build the error responses
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(String error, String message,
                                                                HttpStatus status,
                                                                Map<String, String> fieldErrors) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .fieldErrors(fieldErrors)
                .build();
        return new ResponseEntity<>(response, status);

    }

}
