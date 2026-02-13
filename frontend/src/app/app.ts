import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, Router, NavigationEnd, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AuthService } from './services/auth.service';
import { TransactionService } from './services/transaction.service';
import { Account } from './models/account.model';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  showNavbar = true;
  holderName = signal<string>('User');

  constructor(
    private router: Router,
    private authService: AuthService,
    private transactionService: TransactionService
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      const url = event.urlAfterRedirects || event.url;
      this.showNavbar = !url.includes('/login');

      // Fetch holder name if we are logged in and don't have it yet
      if (this.showNavbar && this.authService.username) {
        this.transactionService.getAccountDetails(this.authService.username).subscribe({
          next: (account) => this.holderName.set(account.holderName),
          error: () => this.holderName.set(this.username) // Fallback to username
        });
      }
    });
  }

  get username(): string {
    const name = this.authService.username;
    return name ? name.charAt(0).toUpperCase() + name.slice(1) : 'User';
  }

  logout(): void {
    this.authService.logout();
  }
}
