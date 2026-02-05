package com.banking.moneytransfer.model.entity;

import com.banking.moneytransfer.exception.AccountNotActiveException;
import com.banking.moneytransfer.exception.InsufficientBalanceException;
import com.banking.moneytransfer.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a bank account
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    private Long id;
    private String holderName;
    private BigDecimal balance;
    private AccountStatus status;
    private Integer version;
    private LocalDateTime lastUpdated;

    /**
     * Debit amount from the account
     * @param amount Amount to debit
     * @throws AccountNotActiveException if account is not active
     * @throws InsufficientBalanceException if balance is insufficient
     */
    public void debit(BigDecimal amount) {
        if (!isActive()) {
            throw new AccountNotActiveException(
                    "Account " + id + " is not active. Current status: " + status
            );
        }

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in account " + id +
                            ". Available: " + balance + ", Required: " + amount
            );
        }

        this.balance = this.balance.subtract(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Credit amount to the account
     * @param amount Amount to credit
     * @throws AccountNotActiveException if account is not active
     */
    public void credit(BigDecimal amount) {
        if (!isActive()) {
            throw new AccountNotActiveException(
                    "Account " + id + " is not active. Current status: " + status
            );
        }

        this.balance = this.balance.add(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Check if account is active
     * @return true if account status is ACTIVE
     */
    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }
}