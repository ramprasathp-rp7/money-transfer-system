import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { TransferRequest } from '../../models/transfer-request.model';
import { Account } from '../../models/account.model';
import { TransferResponse } from '../../models/transfer-response.model';

@Component({
    selector: 'app-transfer',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './transfer.component.html',
    styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {
    transferRequest: TransferRequest = {
        fromAccountId: '1000-1000-1001', // Hardcoded sender for now
        toAccountId: '',
        amount: 0,
        idempotencyKey: ''
    };

    senderAccount = signal<Account | null>(null);
    maskedAccountId = signal<string>('');
    errorMessage = signal<string>('');
    successMessage = signal<string>('');
    lastTransaction = signal<TransferResponse | null>(null);
    isLoading = signal<boolean>(false);

    constructor(private transactionService: TransactionService) { }

    ngOnInit(): void {
        // Generate Idempotency Key
        this.transferRequest.idempotencyKey = crypto.randomUUID();
        console.log('Idempotency Key generated:', this.transferRequest.idempotencyKey);

        // Fetch Sender Account Details
        this.fetchSenderDetails();
    }

    fetchSenderDetails(): void {
        this.transactionService.getAccountDetails(this.transferRequest.fromAccountId).subscribe({
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

        this.transactionService.transfer(this.transferRequest).subscribe({
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
                this.errorMessage.set(error.error?.message || 'Transfer failed. Please try again.');
                this.isLoading.set(false);
            }
        });
    }
}
