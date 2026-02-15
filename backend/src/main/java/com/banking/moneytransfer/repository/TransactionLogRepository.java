package com.banking.moneytransfer.repository;

import com.banking.moneytransfer.model.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("""
       SELECT t FROM TransactionLog t
       WHERE t.createdOn BETWEEN :start AND :end
    """)
    List<TransactionLog> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}