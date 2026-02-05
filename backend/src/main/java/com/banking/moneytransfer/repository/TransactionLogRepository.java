package com.banking.moneytransfer.repository;

import com.banking.moneytransfer.model.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for TransactionLog entity
 */
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {

    /**
     * Find transaction by idempotency key
     * @param idempotencyKey Unique idempotency key
     * @return Optional TransactionLog
     */
    Optional<TransactionLog> findByIdempotencyKey(String idempotencyKey);

    /**
     * Find all transactions for a specific account (either from or to)
     * @param accountId Account ID
     * @return List of transactions
     */
    List<TransactionLog> findByFromAccountIdOrToAccountIdOrderByCreatedOnDesc(
            Long accountId, Long accountId2
    );
}