package com.banking.moneytransfer.controller;

import com.banking.moneytransfer.dto.AccountBalanceResponse;
import com.banking.moneytransfer.dto.AccountResponse;
import com.banking.moneytransfer.model.entity.TransactionLog;
import com.banking.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id) {
        log.info("Received request to get account with ID: {}", id);

        return accountService.getAccount(id);
    }

    /**
     * Get account balance
     * GET /api/v1/accounts/{id}/balance
     */
    @GetMapping("/{id}/balance")
    public AccountBalanceResponse getBalance(@PathVariable Long id) {
        log.info("Received request to get balance for account ID: {}", id);

        return accountService.getBalance(id);
    }

    /**
     * Get transaction history
     * GET /api/v1/accounts/{id}/transactions
     */
    @GetMapping("/{id}/transactions")
    public List<TransactionLog> getTransactions(@PathVariable Long id) {
        log.info("Received request to get transactions for account ID: {}", id);

        return accountService.getTransactions(id);
    }
}