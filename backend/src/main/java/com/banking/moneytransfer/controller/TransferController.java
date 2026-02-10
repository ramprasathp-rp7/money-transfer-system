package com.banking.moneytransfer.controller;

import com.banking.moneytransfer.dto.TransferRequest;
import com.banking.moneytransfer.dto.TransferResponse;
import com.banking.moneytransfer.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for transfer operations
 */
@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    /**
     * Execute fund transfer
     * POST /api/v1/transfers
     */
    @PreAuthorize("#request.fromAccountId == authentication.name")
    @PostMapping
    public TransferResponse transfer(@Valid @RequestBody TransferRequest request) {
        log.info("Received transfer request from account {} to account {}",
                request.getFromAccountId(), request.getToAccountId());

        return transferService.transfer(request);
    }
}