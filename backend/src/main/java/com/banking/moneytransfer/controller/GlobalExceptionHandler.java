package com.banking.moneytransfer.controller;

import com.banking.moneytransfer.dto.ErrorResponse;
import com.banking.moneytransfer.exception.AccountNotActiveException;
import com.banking.moneytransfer.exception.AccountNotFoundException;
import com.banking.moneytransfer.exception.DuplicateTransferException;
import com.banking.moneytransfer.exception.InsufficientBalanceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all controllers
 */

// basePackage property ensures handleGenericException method don't handle framework specific Exception.
@RestControllerAdvice(basePackages = "com.banking.moneytransfer")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex,
                                                                        HttpServletRequest request) {
        log.error("Account not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotActiveException(AccountNotActiveException ex,
                                                                         HttpServletRequest request) {
        log.error("Account not active: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex,
                                                                            HttpServletRequest request) {
        log.error("Insufficient balance: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateTransferException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateTransferException(DuplicateTransferException ex,
                                                                          HttpServletRequest request) {
        log.error("Duplicate transfer: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                        HttpServletRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(422, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation errors: {}", errors);
        ErrorResponse error = new ErrorResponse(422, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,
                                                                HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(500, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}