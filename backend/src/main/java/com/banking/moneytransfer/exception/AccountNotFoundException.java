package com.banking.moneytransfer.exception;

/**
 * Exception thrown when an account is not found
 */
public class AccountNotFoundException extends RuntimeException {

    private static final int errorCode = 404;

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId);
    }

    public int getErrorCode() {
        return errorCode;
    }
}