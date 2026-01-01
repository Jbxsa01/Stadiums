// src/app/components/reservation-create/reservation-create.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ReservationService } from '../../services/reservation.service';
import { StadiumService } from '../../services/stadium.service';
import { AuthService } from '../../services/auth.service';
import { Reservation, StatutReservation } from '../../models/reservation.model';
import { Stadium } from '../../models/stadium.model';

@Component({
  selector: 'app-reservation-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservation-create.component.html',
  styleUrls: ['./reservation-create.component.css']
})
export class ReservationCreateComponent implements OnInit {
  reservation: Reservation = {
    cin: '',
    date: '',
    heureDebut: '',
    heureFin: '',
    stadeId: 0
  };

  stadium: Stadium | null = null;
  cinFile: File | null = null;
  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private reservationService: ReservationService,
    private stadiumService: StadiumService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const stadiumId = this.route.snapshot.paramMap.get('id');
    if (stadiumId) {
      this.reservation.stadeId = +stadiumId;
      this.loadStadium(+stadiumId);
    }
  }

  loadStadium(id: number): void {
    this.stadiumService.getStadiumById(id).subscribe({
      next: (data) => {
        this.stadium = data;
      },
      error: (error) => {
        console.error('Erreur chargement stade:', error);
        this.errorMessage = 'Erreur lors du chargement du stade';
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.cinFile = file;
    }
  }

  getTodayDate(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }

  calculatePrice(): number {
    if (!this.stadium || !this.reservation.heureDebut || !this.reservation.heureFin) {
      return 0;
    }

    const debut = new Date(`2000-01-01T${this.reservation.heureDebut}`);
    const fin = new Date(`2000-01-01T${this.reservation.heureFin}`);
    const heures = (fin.getTime() - debut.getTime()) / (1000 * 60 * 60);

    return heures > 0 ? heures * this.stadium.pricePerHour : 0;
  }

  onSubmit(): void {
    if (!this.cinFile) {
      this.errorMessage = 'Veuillez télécharger une copie de votre CIN';
      return;
    }

    if (!this.reservation.cin || !this.reservation.date ||
      !this.reservation.heureDebut || !this.reservation.heureFin) {
      this.errorMessage = 'Veuillez remplir tous les champs';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.reservationService.createReservation(this.reservation, this.cinFile).subscribe({
      next: (response) => {
        console.log('Réservation créée:', response);
        alert('Réservation créée avec succès!');
        this.router.navigate(['/reservations']);
      },
      error: (error) => {
        console.error('Erreur création réservation:', error);
        this.errorMessage = error.message || 'Erreur lors de la création de la réservation';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
