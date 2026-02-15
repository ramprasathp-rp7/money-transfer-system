package com.banking.moneytransfer.exception;

/**
 * Exception thrown when an account is not found
 */
public class AccountNotFoundException extends RuntimeException {

    private static final int errorCode = 404;

    public AccountNotFoundException(String accountId) {
        super("Account " + accountId + " doesn't exist");
    }

    public int getErrorCode() {
        return errorCode;
    }
}