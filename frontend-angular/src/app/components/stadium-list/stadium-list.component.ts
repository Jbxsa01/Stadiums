import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { StadiumService } from '../../services/stadium.service';
import { Stadium } from '../../models/stadium.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-stadium-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stadium-list.component.html',
  styleUrls: ['./stadium-list.component.css']
})
export class StadiumListComponent implements OnInit {
  stadiums: Stadium[] = [];
  loading: boolean = false;
  errorMessage: string = '';

  // Filtres
  searchTerm: string = '';
  filterAvailable: boolean | null = null;

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

  get filteredStadiums(): Stadium[] {
    return this.stadiums.filter(stadium => {
      // Filtre par recherche
      const matchesSearch = this.searchTerm.trim() === '' ||
        stadium.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        stadium.location.toLowerCase().includes(this.searchTerm.toLowerCase());

      // Filtre par disponibilité
      const matchesAvailability = this.filterAvailable === null ||
        stadium.available === this.filterAvailable;

      return matchesSearch && matchesAvailability;
    });
  }

  reserveStadium(stadium: Stadium): void {
    if (!this.authService.isLoggedIn()) {
      alert('Please sign in to book a stadium');
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
    if (confirm(`Are you sure you want to delete "${stadium.name}"?`)) {
      this.stadiumService.deleteStadium(stadium.id!).subscribe({
        next: () => {
          alert('Stadium deleted successfully');
          this.loadStadiums();
        },
        error: (error) => {
          alert('Error deleting stadium: ' + error.message);
        }
      });
    }
  }
}
