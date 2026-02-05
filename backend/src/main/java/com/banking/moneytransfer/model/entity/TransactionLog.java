package com.banking.moneytransfer.model.entity;

import com.banking.moneytransfer.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a transaction log
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLog {

    private UUID id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private String failureReason;
    private String idempotencyKey;
    private LocalDateTime createdOn;
}