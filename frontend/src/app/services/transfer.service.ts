import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { TransferRequest } from '../models/transfer-request.model';
import { TransferResponse } from '../models/transfer-response.model';

@Injectable({
    providedIn: 'root'
})
export class TransferService {
    private readonly API_ENDPOINT = 'http://localhost:8080/api/v1/transfers';

    constructor(private http: HttpClient) { }

    transfer(transferRequest: TransferRequest): Observable<TransferResponse> {
        return this.http.post<TransferResponse>(this.API_ENDPOINT, transferRequest);
    }
}