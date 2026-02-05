package com.banking.moneytransfer.model;

import com.banking.moneytransfer.exception.AccountNotActiveException;
import com.banking.moneytransfer.exception.InsufficientBalanceException;
import com.banking.moneytransfer.model.entity.Account;
import com.banking.moneytransfer.model.enums.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Account entity
 */
class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .holderName("John Doe")
                .balance(new BigDecimal("1000.00"))
                .status(AccountStatus.ACTIVE)
                .version(0)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    void testDebit_Success() {
        // Given
        BigDecimal debitAmount = new BigDecimal("300.00");
        BigDecimal expectedBalance = new BigDecimal("700.00");

        // When
        account.debit(debitAmount);

        // Then
        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    void testDebit_InsufficientBalance() {
        // Given
        BigDecimal debitAmount = new BigDecimal("1500.00");

        // When & Then
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(debitAmount)
        );

        assertTrue(exception.getMessage().contains("Insufficient balance"));
        assertEquals("TRX-400", exception.getErrorCode());
    }

    @Test
    void testDebit_AccountNotActive() {
        // Given
        account.setStatus(AccountStatus.LOCKED);
        BigDecimal debitAmount = new BigDecimal("100.00");

        // When & Then
        AccountNotActiveException exception = assertThrows(
                AccountNotActiveException.class,
                () -> account.debit(debitAmount)
        );

        assertTrue(exception.getMessage().contains("not active"));
        assertEquals("ACC-403", exception.getErrorCode());
    }

    @Test
    void testCredit_Success() {
        // Given
        BigDecimal creditAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("1500.00");

        // When
        account.credit(creditAmount);

        // Then
        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    void testCredit_AccountNotActive() {
        // Given
        account.setStatus(AccountStatus.CLOSED);
        BigDecimal creditAmount = new BigDecimal("100.00");

        // When & Then
        AccountNotActiveException exception = assertThrows(
                AccountNotActiveException.class,
                () -> account.credit(creditAmount)
        );

        assertTrue(exception.getMessage().contains("not active"));
    }

    @Test
    void testIsActive_WhenActive() {
        // Given
        account.setStatus(AccountStatus.ACTIVE);

        // When & Then
        assertTrue(account.isActive());
    }

    @Test
    void testIsActive_WhenLocked() {
        // Given
        account.setStatus(AccountStatus.LOCKED);

        // When & Then
        assertFalse(account.isActive());
    }

    @Test
    void testIsActive_WhenClosed() {
        // Given
        account.setStatus(AccountStatus.CLOSED);

        // When & Then
        assertFalse(account.isActive());
    }
}