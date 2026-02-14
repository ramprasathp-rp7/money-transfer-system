import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { single } from 'rxjs';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    loginForm!: FormGroup;
    submitted = signal(false);
    errorMessage = signal("");

    constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

    ngOnInit() {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
            password: ['', [Validators.required, Validators.minLength(8)]]
        });
    }

    onSubmit() {
        this.submitted.set(true)
        this.errorMessage.set("")
        if (this.loginForm.valid) {
            const { username, password } = this.loginForm.value;
            this.authService.login(username, password).subscribe({
                next: (response) => {
                    this.submitted.set(false)
                    if (response.ok) {
                        this.router.navigate(['/transactions']);
                    } else {
                        this.errorMessage.set("Invalid credentials")
                    }
                },
                error: (err) => {
                    this.submitted.set(false)
                    this.errorMessage.set("Invalid credentials")
                }
            });
        }
    }
}
