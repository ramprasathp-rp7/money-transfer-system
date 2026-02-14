import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, Router, NavigationEnd, RouterLinkActive } from '@angular/router';
import { CommonModule, NgClass, NgIf } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AuthService } from './services/auth.service';
import { AccountService } from './services/account.service';
import { Navbar } from "./components/navbar/navbar";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, Navbar, NgIf],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  showNavbar = false;
  holderName = signal<string>('User');

  constructor(
    private router: Router,
    private authService: AuthService,
    private accountService: AccountService
  ) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const url = event.urlAfterRedirects || event.url;
        this.showNavbar = !url.includes('/login');

        if (this.authService.loggedIn) {
          this.accountService.fetchAccount(this.authService.accountId!).subscribe({
            next: (account) => this.holderName.set(account.holderName),
            error: () => this.holderName.set(this.authService.accountId || 'User')
          });
        }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
