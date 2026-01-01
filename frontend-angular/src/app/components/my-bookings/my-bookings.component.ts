import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReservationService } from '../../services/reservation.service';
import { StadiumService } from '../../services/stadium.service';
import { Reservation } from '../../models/reservation.model';
import { Stadium } from '../../models/stadium.model';
import { generateTicketPDF } from './ticket.util';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.css']
})
export class MyBookingsComponent implements OnInit {
  reservations: Reservation[] = [];
  stadiums: Stadium[] = [];
  loading = true;
  errorMessage = '';

  searchTerm = '';
  statusFilter: string = 'ALL';

  constructor(
    private reservationService: ReservationService,
    private stadiumService: StadiumService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.stadiumService.getAllStadiums().subscribe({
      next: (stadiums) => {
        this.stadiums = stadiums;
        this.reservationService.getAllReservations().subscribe({
          next: (data) => {
            this.reservations = data;
            console.log('Réservations chargées:', this.reservations);
            this.loading = false;
          },
          error: (err) => {
            this.errorMessage = err.message || 'Erreur lors du chargement des réservations';
            this.loading = false;
          }
        });
      },
      error: (err) => {
        this.errorMessage = err.message || 'Erreur lors du chargement des stades';
        this.loading = false;
      }
    });
  }

  getStadiumById(id: number): Stadium | undefined {
    return this.stadiums.find(s => s.id === id);
  }

  downloadTicket(reservation: Reservation) {
    const stadium = this.getStadiumById(reservation.stadeId);
    if (stadium) {
      generateTicketPDF(reservation, stadium);
    }
  }

  setStatusFilter(status: string) {
    this.statusFilter = status;
  }

  get filteredReservations() {
    return this.reservations.filter(res => {
      const stadium = this.getStadiumById(res.stadeId);
      const matchesSearch = this.searchTerm.trim() === '' ||
        (stadium && stadium.name.toLowerCase().includes(this.searchTerm.toLowerCase()));
      const matchesStatus = this.statusFilter === 'ALL' || res.statut === this.statusFilter;
      return matchesSearch && matchesStatus;
    });
  }
}
