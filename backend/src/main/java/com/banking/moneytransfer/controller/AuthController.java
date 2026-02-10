package com.banking.moneytransfer.controller;

import com.banking.moneytransfer.dto.AccountResponse;
import com.banking.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AccountService accountService;

    @GetMapping("/login")
    public AccountResponse login(Principal principal) {
        // Since this endpoint is authenticated, if execution reaches here,
        // the user is already logged in securely.
        // We just return their details so the Frontend can store the ID.
        String accountId = principal.getName();
        log.info("User logged in successfully: {}", accountId);
        return accountService.getAccount(accountId);
    }
}