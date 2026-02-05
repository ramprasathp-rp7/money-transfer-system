package com.banking.moneytransfer.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransferRequest validation
 */
class TransferRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        // Given
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal("500.00"))
                .idempotencyKey("test-key-123")
                .build();

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidAmount_Null() {
        // Given
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(null)
                .idempotencyKey("test-key-123")
                .build();

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Amount is required"));
    }

    @Test
    void testInvalidAmount_Zero() {
        // Given
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(BigDecimal.ZERO)
                .idempotencyKey("test-key-123")
                .build();

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("greater than 0"));
    }

    @Test
    void testNullFromAccountId() {
        // Given
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(null)
                .toAccountId(2L)
                .amount(new BigDecimal("500.00"))
                .idempotencyKey("test-key-123")
                .build();

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("From account ID is required"));
    }

    @Test
    void testNullToAccountId() {
        // Given
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(null)
                .amount(new BigDecimal("500.00"))
                .idempotencyKey("test-key-123")
                .build();

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("To account ID is required"));
    }

    @Test
    void testNullIdempotencyKey() {
        // Given
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal("500.00"))
                .idempotencyKey(null)
                .build();

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("Idempotency key is required"));
    }
}