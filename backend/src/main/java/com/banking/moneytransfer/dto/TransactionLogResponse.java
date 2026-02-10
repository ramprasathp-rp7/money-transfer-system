package com.banking.moneytransfer.dto;

import com.banking.moneytransfer.model.enums.TransactionStatus;
import com.banking.moneytransfer.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionLogResponse {

    private TransactionType type;
    private String accountId;
    private String holderName;
    private BigDecimal amount;
    private TransactionStatus status;
    private String failureReason;
    private LocalDateTime createdOn;
}
