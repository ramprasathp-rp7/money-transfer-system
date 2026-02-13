import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';
import { Account } from '../models/account.model';
import { TransferRequest } from '../models/transfer-request.model';
import { TransferResponse } from '../models/transfer-response.model';

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    private baseUrl = 'http://localhost:8080/api/v1/accounts';

    constructor(private http: HttpClient) { }

    getTransactions(accountId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.baseUrl}/${accountId}/transactions`);
    }

    getAccountDetails(accountId: string): Observable<Account> {
        return this.http.get<Account>(`${this.baseUrl}/${accountId}`);
    }

    transfer(transferRequest: TransferRequest): Observable<TransferResponse> {
        const transferUrl = 'http://localhost:8080/api/v1/transfers';
        return this.http.post<TransferResponse>(transferUrl, transferRequest);
    }
}

