import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Account } from '../../models/account.model';
import { TransferRequest } from '../../models/transfer-request.model';
import { TransferResponse } from '../../models/transfer-response.model';

import { AuthService } from '../../services/auth.service';
import { AccountService } from 'app/services/account.service';
import { TransferService } from '../../services/transfer.service';

@Component({
    selector: 'app-transfer',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './transfer.component.html',
    styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {
    transferRequest: TransferRequest = {
        fromAccountId: '',
        toAccountId: '',
        amount: 0,
        idempotencyKey: ''
    };

    senderAccount = signal<Account | null>(null);
    maskedAccountId = signal<string>('');
    errorMessage = signal<string>('');
    successMessage = signal<string>('');
    lastTransaction = signal<TransferResponse | null>(null);
    failedTransaction = signal<any | null>(null);
    showBalance = signal<boolean>(false);
    isLoading = signal<boolean>(false);

    constructor(
        private authService: AuthService,
        private accountService: AccountService,
        private transferService: TransferService
    ) { }

    toggleBalance(): void {
        this.showBalance.update(v => !v);
    }

    ngOnInit(): void {
        // Set from account ID from authenticated user
        this.transferRequest.fromAccountId = this.authService.accountId || '';

        // Generate Idempotency Key
        this.transferRequest.idempotencyKey = crypto.randomUUID();
        console.log('Idempotency Key generated:', this.transferRequest.idempotencyKey);

        // Fetch Sender Account Details
        this.fetchSenderDetails();
    }

    fetchSenderDetails(): void {
        this.accountService.fetchAccount(this.transferRequest.fromAccountId).subscribe({
            next: (account) => {
                this.senderAccount.set(account);
                this.maskedAccountId.set(this.maskAccountId(account.id));
            },
            error: (error) => {
                console.error('Error fetching sender details:', error);
                this.errorMessage.set('Failed to load sender account details.');
            }
        });
    }

    maskAccountId(accountId: string): string {
        if (!accountId || accountId.length < 4) return accountId;
        const last4 = accountId.slice(-4);
        return `****${last4}`;
    }

    onSubmit(): void {
        if (!this.senderAccount()) {
            this.errorMessage.set('Sender details not loaded cannot proceed');
            return;
        }

        this.isLoading.set(true);
        this.errorMessage.set('');
        this.successMessage.set('');
        this.lastTransaction.set(null);
        this.failedTransaction.set(null);

        this.transferService.transfer(this.transferRequest).subscribe({
            next: (response) => {
                this.successMessage.set('Transfer successful!');
                this.lastTransaction.set(response);
                this.isLoading.set(false);
                // Reset form but keep sender details and generate new key
                this.transferRequest.toAccountId = '';
                this.transferRequest.amount = 0;
                this.transferRequest.idempotencyKey = crypto.randomUUID();
                // Refresh sender details to show updated balance
                this.fetchSenderDetails();
            },
            error: (error) => {
                console.error('Transfer failed:', error);
                this.failedTransaction.set({
                    message: error.error?.message || 'Transfer could not be processed.',
                    timestamp: new Date(),
                    toAccountId: this.transferRequest.toAccountId,
                    amount: this.transferRequest.amount
                });
                this.isLoading.set(false);

                // IMPORTANT: Generate new idempotency key even on failure 
                // so the next attempt is a fresh transaction
                this.transferRequest.idempotencyKey = crypto.randomUUID();
            }
        });
    }

    resetForm(): void {
        this.lastTransaction.set(null);
        this.failedTransaction.set(null);
        this.successMessage.set('');
        this.errorMessage.set('');
        this.transferRequest.toAccountId = '';
        this.transferRequest.amount = 0;
    }
}
