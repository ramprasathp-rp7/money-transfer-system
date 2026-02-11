import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/v1/auth/login';

    public username: string | null = null;
    public password: string | null = null;

    constructor(private http: HttpClient, private router: Router) { }

    login(username: string, password: string): Observable<HttpResponse<any>> {
        const headers = new HttpHeaders({
            'username': username,
            'password': password
        });

        return this.http.get(this.apiUrl, { headers, observe: 'response' }).pipe(
            tap(response => {
                if (response.status === 204) {
                    this.username = username;
                    this.password = password;
                    this.router.navigate(['/transactions']);
                }
            }),
            catchError(error => {
                alert('Login failed. Please check your credentials.');
                return throwError(() => error);
            })
        );
    }
}
