import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly API_ENDPOINT = 'http://localhost:8080/api/v1/auth/login';

    public username: string | null = null;
    public password: string | null = null;
    public loggedIn: boolean = false;

    constructor(private http: HttpClient) { }

    login(username: string, password: string): Observable<HttpResponse<any>> {
        this.username = username;
        this.password = password;

        return this.http.get(this.API_ENDPOINT, {
            observe: 'response',
        }).pipe(
            tap(response => {
                if (response.ok) {
                    this.loggedIn = true;
                } else {
                    this.loggedIn = false;
                    this.username = null;
                    this.password = null;
                }
            }),
            catchError((err) => {
                this.loggedIn = false;
                this.username = null;
                this.password = null;
                return throwError(() => err)
            })
        );
    }

    logout(): void {
        this.loggedIn = false;
        this.username = null;
        this.password = null;
    }
}
