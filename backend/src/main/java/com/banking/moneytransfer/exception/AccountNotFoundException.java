package com.banking.moneytransfer.exception;

/**
 * Exception thrown when an account is not found
 */
public class AccountNotFoundException extends RuntimeException {

    private final String errorCode = "ACC-404";

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId);
    }

    public String getErrorCode() {
        return errorCode;
    }
}