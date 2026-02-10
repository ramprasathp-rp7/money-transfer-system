package com.banking.moneytransfer.controller;

import com.banking.moneytransfer.dto.AccountBalanceResponse;
import com.banking.moneytransfer.dto.AccountResponse;
import com.banking.moneytransfer.dto.TransactionLogResponse;
import com.banking.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * REST Controller for account operations
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /**
     * Get account details
     * GET /api/v1/accounts/{id}
     */
    @PreAuthorize("#id == authentication.name")
    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable String id) {
        log.info("Received request to get account with ID: {}", id);

        return accountService.getAccount(id);
    }

    /**
     * Get account balance
     * GET /api/v1/accounts/{id}/balance
     */
    @PreAuthorize("#id == authentication.name")
    @GetMapping("/{id}/balance")
    public AccountBalanceResponse getBalance(@PathVariable String id) {
        log.info("Received request to get balance for account ID: {}", id);

        return accountService.getBalance(id);
    }

    /**
     * Get transaction history
     * GET /api/v1/accounts/{id}/transactions
     */
    @PreAuthorize("#id == authentication.name")
    @GetMapping("/{id}/transactions")
    public List<TransactionLogResponse> getTransactions(@PathVariable String id) {
        log.info("Received request to get transactions for account ID: {}", id);

        return accountService.getTransactions(id);
    }
}