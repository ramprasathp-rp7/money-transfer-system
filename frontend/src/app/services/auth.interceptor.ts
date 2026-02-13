import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);

    // Only add auth header if we have credentials
   
    if (authService.username && authService.password) {
       
        const authHeader = 'Basic ' + btoa(authService.username + ':' + authService.password);

        const authReq = req.clone({
            setHeaders: {
                Authorization: authHeader,
                Accept: 'application/json'
            }
        });
        
        return next(authReq);
    }

    console.log("⚠️ No credentials found, request sent without Authorization header");
    return next(req);
};
