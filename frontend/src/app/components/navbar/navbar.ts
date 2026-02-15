import { Component, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AccountService } from 'app/services/account.service';
import { AuthService } from 'app/services/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  constructor(private authService: AuthService, private accountService: AccountService, private router: Router) {}

  username = signal<string>('User');

  ngOnInit() {
    if (this.authService.loggedIn) {
    this.accountService.fetchAccount(this.authService.accountId!).subscribe({
      next: (account) => this.username.set(account.holderName),
      error: () => this.router.navigate(['/login'])
    });
    }
  }

  get greeting(): string {
      const hour = new Date().getHours();
      if (hour < 12) return 'Good Morning';
      if (hour < 17) return 'Good Afternoon';
      return 'Good Evening';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
