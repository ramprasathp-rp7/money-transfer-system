package com.banking.moneytransfer.exception;

/**
 * Exception thrown when account has insufficient balance for a transaction
 */
public class InsufficientBalanceException extends RuntimeException {

    private final String errorCode = "TRX-400";

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}