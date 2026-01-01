// src/app/components/navbar/navbar.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    const user = this.authService.currentUserValue;
    if (user) {
      this.authService.logout(user.userId).subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Erreur lors de la déconnexion:', error);
          this.router.navigate(['/login']);
        }
      });
    }
  }

  get currentUser() {
    return this.authService.currentUserValue;
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  get isAdmin() {
    return this.authService.isAdmin();
  }
}
