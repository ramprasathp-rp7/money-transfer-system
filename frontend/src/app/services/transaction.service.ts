import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';
import { Account } from '../models/account.model';
import { TransferRequest } from '../models/transfer-request.model';
import { TransferResponse } from '../models/transfer-response.model';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    private baseUrl = 'http://localhost:8080/api/v1/accounts';

    constructor(private http: HttpClient, private authService: AuthService) { }

    private getHeaders(): HttpHeaders {
        const username = this.authService.username || '';
        const password = this.authService.password || '';
        const authHeader = 'Basic ' + btoa(username + ':' + password);

        return new HttpHeaders({
            'Authorization': authHeader,
            'Accept': 'application/json'
        });
    }

    getTransactions(accountId: string): Observable<Transaction[]> {
        const headers = this.getHeaders();
        return this.http.get<Transaction[]>(`${this.baseUrl}/${accountId}/transactions`, { headers });
    }

    getAccountDetails(accountId: string): Observable<Account> {
        const headers = this.getHeaders();
        return this.http.get<Account>(`${this.baseUrl}/${accountId}`, { headers });
    }

    transfer(transferRequest: TransferRequest): Observable<TransferResponse> {
        const headers = this.getHeaders();
        const transferUrl = 'http://localhost:8080/api/v1/transfers';
        return this.http.post<TransferResponse>(transferUrl, transferRequest, { headers });
    }
}

