package com.banking.moneytransfer.service;

import com.banking.moneytransfer.dto.AccountBalanceResponse;
import com.banking.moneytransfer.dto.AccountResponse;
import com.banking.moneytransfer.exception.AccountNotFoundException;
import com.banking.moneytransfer.model.entity.Account;
import com.banking.moneytransfer.model.entity.TransactionLog;
import com.banking.moneytransfer.repository.AccountRepository;
import com.banking.moneytransfer.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for account operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    /**
     * Get account by ID
     * @param id Account ID
     * @return AccountResponse
     * @throws AccountNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long id) {
        log.info("Fetching account with ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return mapToResponse(account);
    }

    /**
     * Get account balance
     * @param id Account ID
     * @return Account balance
     * @throws AccountNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public AccountBalanceResponse getBalance(Long id) {
        log.info("Fetching balance for account ID: {}", id);

        return accountRepository.findById(id)
                .map(account -> new AccountBalanceResponse(account.getBalance()))
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Get transaction history for an account
     * @param id Account ID
     * @return List of transactions
     * @throws AccountNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public List<TransactionLog> getTransactions(Long id) {
        log.info("Fetching transactions for account ID: {}", id);

        // Verify account exists
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }

        return transactionLogRepository.findByFromAccountIdOrToAccountIdOrderByCreatedOnDesc(id, id);
    }

    /**
     * Map Account entity to AccountResponse DTO
     */
    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .lastUpdated(account.getLastUpdated())
                .build();
    }
}