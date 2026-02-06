package com.banking.moneytransfer.exception;

/**
 * Exception thrown when a duplicate transfer is attempted using same idempotency key.
 * Compliant with Requirement 17.1 (TRX-409)
 */
public class DuplicateTransferException extends RuntimeException {

    private static final int errorCode = 409;

    // Standard constructor for custom messages
    public DuplicateTransferException(String message) {
        super(message);
    }

    // Static factory method to handle the idempotency key logic
    public static DuplicateTransferException withKey(String idempotencyKey) {
        return new DuplicateTransferException("Duplicate transfer detected with idempotency key: " + idempotencyKey);
    }

    public int getErrorCode() {
        return errorCode;
    }
}