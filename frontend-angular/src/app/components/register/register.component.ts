// src/app/components/register/register.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,  // ✅ IMPORTANT : Component standalone
  imports: [CommonModule, FormsModule],  // ✅ Import des modules nécessaires
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerRequest = {
    username: '',
    email: '',
    password: ''
  };

  confirmPassword = '';
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private router: Router) {}

  onSubmit() {
    // Réinitialiser les messages
    this.errorMessage = '';
    this.successMessage = '';

    // Vérifier que les mots de passe correspondent
    if (this.registerRequest.password !== this.confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas';
      return;
    }

    // Vérifier la longueur du mot de passe
    if (this.registerRequest.password.length < 6) {
      this.errorMessage = 'Le mot de passe doit contenir au moins 6 caractères';
      return;
    }

    this.loading = true;

    // 📦 SIMULATION - Remplacer par votre appel API réel
    console.log('📝 Inscription:', this.registerRequest);

    setTimeout(() => {
      this.loading = false;
      this.successMessage = 'Inscription réussie ! Redirection...';

      // Rediriger vers la page de connexion après 2 secondes
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    }, 1500);

    /*
    // 🔥 CODE RÉEL avec AuthService (décommenter quand le backend est prêt)
    this.authService.register(this.registerRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.successMessage = 'Inscription réussie ! Redirection...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Une erreur est survenue';
        console.error('Erreur d\'inscription:', error);
      }
    });
    */
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
