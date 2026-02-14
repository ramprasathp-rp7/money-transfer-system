import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly API_ENDPOINT = 'http://localhost:8080/api/v1/auth/login';

    public accountId: string | null = null;
    public authToken: string | null = null;
    public loggedIn: boolean = false;

    constructor(private http: HttpClient) { }

    login(username: string, password: string): Observable<HttpResponse<any>> {
        this.accountId = username;
        this.authToken = 'Basic ' + btoa(username + ':' + password);

        return this.http.get(this.API_ENDPOINT, {
            observe: 'response',
        }).pipe(
            tap(response => {
                if (response.ok) {
                    this.loggedIn = true;
                } else {
                    this.logout();
                }
            }),
            catchError((err) => {
                this.logout();
                return throwError(() => err);
            })
        );
    }

    logout(): void {
        this.loggedIn = false;
        this.accountId = null;
        this.authToken = null;
    }
}
