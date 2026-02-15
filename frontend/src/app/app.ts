import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { filter } from 'rxjs/operators';
import { Navbar } from "./components/navbar/navbar";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, Navbar, NgIf],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  showNavbar = false;

  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const url = event.urlAfterRedirects || event.url;
        this.showNavbar = !url.includes('/login');
    });
  }
}
