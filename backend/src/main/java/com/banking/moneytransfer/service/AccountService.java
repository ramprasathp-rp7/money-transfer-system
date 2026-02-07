package com.banking.moneytransfer.service;

import com.banking.moneytransfer.dto.AccountBalanceResponse;
import com.banking.moneytransfer.dto.AccountResponse;
import com.banking.moneytransfer.dto.TransactionLogResponse;
import com.banking.moneytransfer.exception.AccountNotFoundException;
import com.banking.moneytransfer.model.entity.Account;
import com.banking.moneytransfer.model.enums.TransactionType;
import com.banking.moneytransfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service class for account operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Get account by ID
     *
     * @param id Account ID
     * @return AccountResponse
     * @throws AccountNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public AccountResponse getAccount(String id) {
        log.info("Fetching account with ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return mapToResponse(account);
    }

    /**
     * Get account balance
     *
     * @param id Account ID
     * @return Account balance
     * @throws AccountNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public AccountBalanceResponse getBalance(String id) {
        log.info("Fetching balance for account ID: {}", id);

        return accountRepository.findById(id)
                .map(account -> new AccountBalanceResponse(account.getBalance()))
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Get transaction history for an account
     *
     * @param id Account ID
     * @return List of transactions (order by createdOn)
     * @throws AccountNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public List<TransactionLogResponse> getTransactions(String id) {
        log.info("Fetching transactions for account ID: {}", id);

        // Verify account exists
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        // List send and receive transaction, map to TransactionLogResponse object, and
        // Sort by CreateOn timestamp (newest to oldest)
        return Stream.concat(
                        account.getSentTransactions()
                                .stream()
                                .map(t -> TransactionLogResponse.builder()
                                        .accountId(t.getToAccount().getId())
                                        .holderName(t.getToAccount().getHolderName())
                                        .type(TransactionType.SEND)
                                        .status(t.getStatus())
                                        .amount(t.getAmount())
                                        .createdOn(t.getCreatedOn())
                                        .build()),
                        account.getReceivedTransactions()
                                .stream()
                                .map(t -> TransactionLogResponse.builder()
                                        .accountId(t.getFromAccount().getId())
                                        .holderName(t.getFromAccount().getHolderName())
                                        .type(TransactionType.RECEIVE)
                                        .status(t.getStatus())
                                        .amount(t.getAmount())
                                        .createdOn(t.getCreatedOn())
                                        .build())
                )
                .sorted(Comparator.comparing(TransactionLogResponse::getCreatedOn).reversed())
                .toList();
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