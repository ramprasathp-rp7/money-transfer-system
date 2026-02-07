package com.banking.moneytransfer.model.entity;

import com.banking.moneytransfer.exception.AccountNotActiveException;
import com.banking.moneytransfer.exception.InsufficientBalanceException;
import com.banking.moneytransfer.model.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Entity representing a bank account
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(length = 16)
    private String id;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Version
    @Column(nullable = false)
    private Integer version;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @JsonIgnore
    @OneToMany(targetEntity = TransactionLog.class, mappedBy = "fromAccount")
    private List<TransactionLog> sentTransactions;

    @JsonIgnore
    @OneToMany(targetEntity = TransactionLog.class, mappedBy = "toAccount")
    private List<TransactionLog> receivedTransactions;

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