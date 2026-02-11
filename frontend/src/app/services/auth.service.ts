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
        // Create the standard Basic Auth string: "Basic base64(username:password)"
        const authHeader = 'Basic ' + btoa(username + ':' + password);

        const headers = new HttpHeaders({
            'Authorization': authHeader,
            'Accept': 'application/json'
        });

        return this.http.get(this.apiUrl, {
            headers,
            observe: 'response',
            withCredentials: true // Important for maintaining the session
        }).pipe(
            tap(response => {
                // Note: If login is successful, Spring usually returns 200 OK
                // Change status check if your specific controller returns 204
                if (response.status === 200 || response.status === 204) {
                    this.username = username;
                    this.password = password;
                    this.router.navigate(['/transactions']);
                }
            }),
            catchError(error => {
                console.error('Login Error:', error);
                alert('Login failed. Please check your credentials.');
                return throwError(() => error);
            })
        );
    }
}
