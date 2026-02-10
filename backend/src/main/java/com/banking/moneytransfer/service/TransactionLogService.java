package com.banking.moneytransfer.service;

import com.banking.moneytransfer.model.entity.TransactionLog;
import com.banking.moneytransfer.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionLogService {

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    // REQUIRES_NEW suspends the current transaction (the one about to rollback)
    // and creates a fresh transaction just for this save operation.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(TransactionLog logEntry) {
        transactionLogRepository.save(logEntry);
    }
}