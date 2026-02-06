package com.banking.moneytransfer.service;

import com.banking.moneytransfer.dto.TransferRequest;
import com.banking.moneytransfer.dto.TransferResponse;
import com.banking.moneytransfer.exception.AccountNotFoundException;
import com.banking.moneytransfer.exception.DuplicateTransferException;
import com.banking.moneytransfer.model.entity.Account;
import com.banking.moneytransfer.model.entity.TransactionLog;
import com.banking.moneytransfer.model.enums.TransactionStatus;
import com.banking.moneytransfer.repository.AccountRepository;
import com.banking.moneytransfer.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for fund transfer operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    /**
     * Execute fund transfer between accounts
     * @param request Transfer request
     * @return TransferResponse
     */
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        log.info("Starting transfer from account {} to account {} for amount {}",
                request.getFromAccountId(), request.getToAccountId(), request.getAmount());

        // Validate transfer request
        validateTransfer(request);

        // Check for duplicate transfer (idempotency)
        checkDuplicateTransfer(request.getIdempotencyKey());

        // Execute transfer
        try {
            return executeTransfer(request);
        } catch (Exception e) {
            log.error("Transfer failed: {}", e.getMessage());

            // Log failed transaction
            logFailedTransaction(request, e.getMessage());

            throw e;
        }
    }

    /**
     * Validate transfer request
     */
    private void validateTransfer(TransferRequest request) {
        // Rule 1: Accounts must be different
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        // Rule 6: Amount must be greater than 0
        if (request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than 0");
        }
    }

    /**
     * Check for duplicate transfer using idempotency key
     */
    private void checkDuplicateTransfer(String idempotencyKey) {
        Optional<TransactionLog> existingTransaction =
                transactionLogRepository.findByIdempotencyKey(idempotencyKey);

        if (existingTransaction.isPresent()) {
            log.warn("Duplicate transfer detected with idempotency key: {}", idempotencyKey);
            throw DuplicateTransferException.withKey(idempotencyKey);
        }
    }

    /**
     * Execute the actual transfer
     */
    private TransferResponse executeTransfer(TransferRequest request) {
        // Fetch and lock accounts (pessimistic locking for concurrency control)
        Account fromAccount = accountRepository.findByIdWithLock(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));

        Account toAccount = accountRepository.findByIdWithLock(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccountId()));

        // Rule 9: Debit before credit
        fromAccount.debit(request.getAmount());
        toAccount.credit(request.getAmount());

        // Save accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Rule 10: Log transaction
        TransactionLog transactionLog = logSuccessfulTransaction(request);

        log.info("Transfer completed successfully. Transaction ID: {}", transactionLog.getId());

        // Build response
        return TransferResponse.builder()
                .transactionId(transactionLog.getId().toString())
                .status("SUCCESS")
                .message("Transfer completed successfully")
                .debitedFrom(request.getFromAccountId())
                .creditedTo(request.getToAccountId())
                .amount(request.getAmount())
                .build();
    }

    /**
     * Log successful transaction
     */
    private TransactionLog logSuccessfulTransaction(TransferRequest request) {
        TransactionLog log = TransactionLog.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .status(TransactionStatus.SUCCESS)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        return transactionLogRepository.save(log);
    }

    /**
     * Log failed transaction
     */
    private void logFailedTransaction(TransferRequest request, String failureReason) {
        TransactionLog log = TransactionLog.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .status(TransactionStatus.FAILED)
                .failureReason(failureReason)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        transactionLogRepository.save(log);
    }
}