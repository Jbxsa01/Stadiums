// src/app/components/stadium-list/stadium-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StadiumService } from '../../services/stadium.service';
import { Stadium } from '../../models/stadium.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-stadium-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stadium-list.component.html',
  styleUrls: ['./stadium-list.component.css']
})
export class StadiumListComponent implements OnInit {
  stadiums: Stadium[] = [];
  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private stadiumService: StadiumService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadStadiums();
  }

  loadStadiums(): void {
    this.loading = true;
    this.errorMessage = '';

    this.stadiumService.getAllStadiums().subscribe({
      next: (data) => {
        this.stadiums = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur chargement stades:', error);
        this.errorMessage = error.message || 'Erreur lors du chargement des stades';
        this.loading = false;
      }
    });
  }

  reserveStadium(stadium: Stadium): void {
    if (!this.authService.isLoggedIn()) {
      alert('Veuillez vous connecter pour réserver un stade');
      this.router.navigate(['/login']);
      return;
    }
    this.router.navigate(['/reservation', stadium.id]);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  editStadium(stadium: Stadium): void {
    this.router.navigate(['/admin/stadium/edit', stadium.id]);
  }

  deleteStadium(stadium: Stadium): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le stade "${stadium.name}" ?`)) {
      this.stadiumService.deleteStadium(stadium.id!).subscribe({
        next: () => {
          alert('Stade supprimé avec succès');
          this.loadStadiums();
        },
        error: (error) => {
          alert('Erreur lors de la suppression: ' + error.message);
        }
      });
    }
  }
}
