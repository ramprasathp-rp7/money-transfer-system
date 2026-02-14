import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Account } from '../models/account.model';
import { Transaction } from '../models/transaction.model';

@Injectable({
    providedIn: 'root'
})
export class AccountService {
    private readonly API_ENDPOINT = 'http://localhost:8080/api/v1/accounts';

    constructor(private http: HttpClient) { }

    fetchAccount(accountId: string): Observable<Account> {
        return this.http.get<Account>(`${this.API_ENDPOINT}/${accountId}`);
    }

    fetchTransactions(accountId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.API_ENDPOINT}/${accountId}/transactions`);
    }
}