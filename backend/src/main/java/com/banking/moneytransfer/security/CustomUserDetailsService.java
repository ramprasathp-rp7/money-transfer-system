package com.banking.moneytransfer.security;

import com.banking.moneytransfer.model.entity.Account;
import com.banking.moneytransfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        // 1. Find the Account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found: " + accountId));

        // 2. Return a Spring Security User object
        // The password here is the HASH from the database.
        // Spring will automatically check the input password against this hash.
        return User.builder()
                .username(account.getId())
                .password(account.getPasswordHash())
                .roles("USER") // Assign a default role
                .build();
    }
}