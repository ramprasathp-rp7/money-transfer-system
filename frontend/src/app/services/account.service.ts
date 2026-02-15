import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Account } from '../models/account.model';
import { Transaction } from '../models/transaction.model';
import { AccountBalance } from 'app/models/account-balance.model';
import { environment } from 'environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AccountService {
    private readonly API_ENDPOINT = environment.baseUrl + '/accounts';

    constructor(private http: HttpClient) { }

    fetchAccount(accountId: string): Observable<Account> {
        return this.http.get<Account>(`${this.API_ENDPOINT}/${accountId}`);
    }

    fetchBalance(accountId: string): Observable<AccountBalance> {
        return this.http.get<AccountBalance>(`${this.API_ENDPOINT}/${accountId}/balance`);
    }

    fetchTransactions(accountId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.API_ENDPOINT}/${accountId}/transactions`);
    }
}