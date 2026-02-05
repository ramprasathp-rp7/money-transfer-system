package com.banking.moneytransfer.exception;

/**
 * Exception thrown when an account is not in ACTIVE status
 */
public class AccountNotActiveException extends RuntimeException {

    private final String errorCode = "ACC-403";

    public AccountNotActiveException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}