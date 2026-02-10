package com.banking.moneytransfer.controller;

import com.banking.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AccountService accountService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void login(Principal principal) {
        /*
            If the controller method is executed, the authentication has been successful.
            We don't need to return any content because this endpoint is used to check whether the
            user credential are valid (status=204) or not (status=401).
         */
        String accountId = principal.getName();
        log.info("User credential are valid: accountId={}", accountId);

        return;
    }
}