import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
    // TODO: In the future, these will be replaced by a real login system
    private username = 'user';
    private password = 'password';

    constructor(private http: HttpClient) { }

    getTransactions(accountId: string): Observable<Transaction[]> {
        const headers = new HttpHeaders({
            'Authorization': 'Basic ' + btoa(this.username + ':' + this.password)
        });

        return this.http.get<Transaction[]>(`${this.baseUrl}/${accountId}/transactions`, { headers });
    }

    getAccountDetails(accountId: string): Observable<Account> {
        const headers = new HttpHeaders({
            'Authorization': 'Basic ' + btoa(this.username + ':' + this.password)
        });
        return this.http.get<Account>(`${this.baseUrl}/${accountId}`, { headers });
    }

    transfer(transferRequest: TransferRequest): Observable<TransferResponse> {
        const headers = new HttpHeaders({
            'Authorization': 'Basic ' + btoa(this.username + ':' + this.password)
        });
        // The transfer endpoint is likely at /api/v1/transfers based on the user request
        // The current baseUrl is .../api/v1/accounts.
        // I need to adjust the URL for the transfer endpoint.
        // Assuming the transfer endpoint is http://localhost:8080/api/v1/transfers
        const transferUrl = 'http://localhost:8080/api/v1/transfers';
        return this.http.post<TransferResponse>(transferUrl, transferRequest, { headers });
    }
}
