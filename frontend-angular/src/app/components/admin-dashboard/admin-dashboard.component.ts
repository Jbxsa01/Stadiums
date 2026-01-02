// src/app/components/admin-dashboard/admin-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReservationService } from '../../services/reservation.service';
import { StadiumService } from '../../services/stadium.service';
import { AuthService } from '../../services/auth.service';
import { Reservation, StatutReservation } from '../../models/reservation.model';
import { Stadium } from '../../models/stadium.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  reservations: Reservation[] = [];
  stadiums: Stadium[] = [];
  loading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private reservationService: ReservationService,
    private stadiumService: StadiumService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Vérifier que l'utilisateur est admin
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.errorMessage = '';

    // Charger les stades d'abord
    this.stadiumService.getAllStadiums().subscribe({
      next: (stadiums) => {
        this.stadiums = stadiums;

        // Puis charger les réservations
        this.reservationService.getAllReservations().subscribe({
          next: (reservations) => {
            this.reservations = reservations;
            console.log('📊 Admin: Réservations chargées:', this.reservations);
            this.loading = false;
          },
          error: (err) => {
            this.errorMessage = 'Erreur lors du chargement des réservations';
            console.error(err);
            this.loading = false;
          }
        });
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors du chargement des stades';
        console.error(err);
        this.loading = false;
      }
    });
  }

  getStadiumById(id: number): Stadium | undefined {
    return this.stadiums.find(s => s.id === id);
  }

  get pendingCount(): number {
    return this.reservations.filter(r => r.statut === StatutReservation.EN_ATTENTE).length;
  }

  get confirmedCount(): number {
    return this.reservations.filter(r => r.statut === StatutReservation.CONFIRMEE).length;
  }

  get cancelledCount(): number {
    return this.reservations.filter(r => r.statut === StatutReservation.ANNULEE).length;
  }

  confirmReservation(reservation: Reservation): void {
    if (!reservation.id) return;

    if (confirm(`Confirmer la réservation #${reservation.id} ?`)) {
      this.reservationService.changeStatut(reservation.id, StatutReservation.CONFIRMEE).subscribe({
        next: (updatedReservation) => {
          this.successMessage = `Réservation #${reservation.id} confirmée avec succès`;
          console.log('✅ Réservation confirmée:', updatedReservation);

          // Mettre à jour localement
          const index = this.reservations.findIndex(r => r.id === reservation.id);
          if (index !== -1) {
            this.reservations[index] = { ...this.reservations[index], statut: StatutReservation.CONFIRMEE };
          }

          // Effacer le message après 3 secondes
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (err) => {
          this.errorMessage = 'Erreur lors de la confirmation';
          console.error(err);
          setTimeout(() => this.errorMessage = '', 3000);
        }
      });
    }
  }

  cancelReservation(reservation: Reservation): void {
    if (!reservation.id) return;

    if (confirm(`Êtes-vous sûr de vouloir refuser la réservation #${reservation.id} ?`)) {
      this.reservationService.changeStatut(reservation.id, StatutReservation.ANNULEE).subscribe({
        next: (updatedReservation) => {
          this.successMessage = `Réservation #${reservation.id} refusée`;
          console.log('❌ Réservation annulée:', updatedReservation);

          // Mettre à jour localement
          const index = this.reservations.findIndex(r => r.id === reservation.id);
          if (index !== -1) {
            this.reservations[index] = { ...this.reservations[index], statut: StatutReservation.ANNULEE };
          }

          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (err) => {
          this.errorMessage = 'Erreur lors de l\'annulation';
          console.error(err);
          setTimeout(() => this.errorMessage = '', 3000);
        }
      });
    }
  }

  viewDetails(reservation: Reservation): void {
    const stadium = this.getStadiumById(reservation.stadeId);
    const details = `
Détails de la réservation #${reservation.id}

Stade: ${stadium?.name || 'N/A'}
Lieu: ${stadium?.location || 'N/A'}
Client (CIN): ${reservation.cin}
Date: ${reservation.date}
Horaire: ${reservation.heureDebut} - ${reservation.heureFin}
Prix: ${reservation.prix || 'N/A'} DH
Statut: ${reservation.statut}
    `;
    alert(details);
  }
}
