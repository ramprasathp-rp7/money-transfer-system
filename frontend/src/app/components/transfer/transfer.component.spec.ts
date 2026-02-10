import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransferComponent } from './transfer.component';
import { TransactionService } from '../../services/transaction.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { describe, it, expect, beforeEach, vi } from 'vitest';

describe('TransferComponent', () => {
    let component: TransferComponent;
    let fixture: ComponentFixture<TransferComponent>;
    let transactionServiceSpy: any;

    beforeEach(async () => {
        const spy = {
            transfer: vi.fn(),
            getAccountDetails: vi.fn()
        };

        // Mock getAccountDetails to return a dummy account
        spy.getAccountDetails.mockReturnValue(of({
            id: '1000-1000-1001',
            holderName: 'Test User',
            balance: 5000,
            status: 'ACTIVE',
            lastUpdated: '2023-01-01'
        }));

        await TestBed.configureTestingModule({
            imports: [TransferComponent, FormsModule, HttpClientTestingModule],
            providers: [
                { provide: TransactionService, useValue: spy }
            ]
        }).compileComponents();

        transactionServiceSpy = TestBed.inject(TransactionService);
        fixture = TestBed.createComponent(TransferComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should generate idempotency key on init', () => {
        expect(component.transferRequest.idempotencyKey).toBeTruthy();
        expect(component.transferRequest.idempotencyKey.length).toBeGreaterThan(0);
    });

    it('should fetch sender details on init', () => {
        expect(transactionServiceSpy.getAccountDetails).toHaveBeenCalledWith('1000-1000-1001');
        expect(component.senderAccount).toBeTruthy();
        expect(component.senderAccount?.holderName).toBe('Test User');
    });

    it('should mask account id correctly', () => {
        const masked = component.maskAccountId('1000-1000-1001');
        expect(masked).toBe('****1001');
    });

    it('should call transfer service on submit', () => {
        // Setup form data
        component.transferRequest.toAccountId = '1000-1000-1002';
        component.transferRequest.amount = 100;

        // Mock transfer response
        transactionServiceSpy.transfer.mockReturnValue(of({
            transactionId: 'test-id',
            status: 'SUCCESS',
            message: 'Transfer successful',
            debitedFrom: '1000-1000-1001',
            creditedTo: '1000-1000-1002',
            amount: 100
        }));

        // Trigger submit
        component.onSubmit();

        expect(transactionServiceSpy.transfer).toHaveBeenCalled();
        expect(component.successMessage).toContain('Transfer successful');
        expect(component.lastTransaction?.transactionId).toBe('test-id');
    });
});
