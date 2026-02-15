import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { TransferRequest } from '../../models/transfer-request.model';
import { TransferResponse } from '../../models/transfer-response.model';

import { AuthService } from '../../services/auth.service';
import { AccountService } from 'app/services/account.service';
import { TransferService } from '../../services/transfer.service';
import { notEqual } from 'app/validators/matches.validator';

@Component({
    selector: 'app-transfer',
    standalone: true,
    imports: [ReactiveFormsModule,CommonModule],
    templateUrl: './transfer.component.html',
    styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {
    transferForm!: FormGroup;
    submitted = signal<boolean>(false);
    isLoading = signal<boolean>(false);

    transferResponse = signal<TransferResponse | null>(null);

    balance = signal<number>(0.0);
    showBalance = signal<boolean>(false);

    failedTransaction = signal<any | null>(null);

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private accountService: AccountService,
        private transferService: TransferService
    ) { }

    toggleBalance(): void {
        this.showBalance.update(v => !v);
    }

    ngOnInit(): void {
        this.transferForm = this.fb.group({ 
            toAccountId: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14), notEqual(this.authService.accountId!)]],
            amount: [null, [Validators.required, Validators.min(0.01)]]
         })

        if (!this.authService.loggedIn) return;

        this.fetchSenderDetails();
    }

    fetchSenderDetails(): void {
        this.accountService.fetchBalance(this.authService.accountId!).subscribe({
            next: (res) => {
                this.balance.set(res.balance);
            }
        });
    }

    onSubmit(): void {
        // will be set to true
        this.submitted.set(true);

        // guard statement
        if (this.transferForm.invalid) return;

        const {toAccountId, amount} = this.transferForm.value;
        const transferRequest: TransferRequest = {
            fromAccountId: this.authService.accountId!,
            toAccountId,
            amount,
            idempotencyKey: crypto.randomUUID()

        }

        this.isLoading.set(true);
        this.transferService.transfer(transferRequest).subscribe({
            next: (response) => {
                this.transferResponse.set(response);
                this.isLoading.set(false);

                // refresh account details to show updated balance
                this.fetchSenderDetails();
            },
            error: (error) => {
                this.failedTransaction.set({
                    message: error.error?.error || "Transaction failed due to unknown exception. Please contact customer support.",
                    timestamp: new Date(),
                    toAccountId: toAccountId,
                    amount: amount,
                });
                this.isLoading.set(false);
            }
        });
    }

    resetForm(): void {
        this.transferResponse.set(null);
        this.failedTransaction.set(null);

        // reset form
        this.transferForm.controls['toAccountId'].setValue('');
        this.transferForm.controls['amount'].setValue(null);
        this.submitted.set(false);
    }
}
