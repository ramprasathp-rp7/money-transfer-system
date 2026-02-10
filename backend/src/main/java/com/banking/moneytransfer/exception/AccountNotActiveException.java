package com.banking.moneytransfer.exception;

/**
 * Exception thrown when an account is not in ACTIVE status
 */
public class AccountNotActiveException extends RuntimeException {

    private static final int errorCode = 403;

    public AccountNotActiveException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }
}