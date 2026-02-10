package com.banking.moneytransfer.exception;

/**
 * Exception thrown when account has insufficient balance for a transaction
 */
public class InsufficientBalanceException extends RuntimeException {

    private static final int errorCode = 400;

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }
}