// src/app/services/reservation.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Reservation, StatutReservation } from '../models/reservation.model';
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = `${environment.apiUrl}/api/reservations`;

  constructor(private http: HttpClient) { }

  createReservation(reservation: Reservation, cinFile: File): Observable<Reservation> {
    const formData = new FormData();

    // Ajouter le fichier CIN
    formData.append('cinFile', cinFile, cinFile.name);

    // Ajouter les données de réservation en JSON
    const reservationBlob = new Blob([JSON.stringify(reservation)], { type: 'application/json' });
    formData.append('reservation', reservationBlob);

    return this.http.post<Reservation>(this.apiUrl, formData)
      .pipe(
        tap(newReservation => console.log('Reservation created:', newReservation)),
        catchError(this.handleError)
      );
  }

  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.apiUrl)
      .pipe(
        tap(reservations => console.log('Reservations retrieved:', reservations.length)),
        catchError(this.handleError)
      );
  }

  getReservationById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(reservation => console.log('Reservation retrieved:', reservation)),
        catchError(this.handleError)
      );
  }

  updateReservation(id: number, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}`, reservation)
      .pipe(
        tap(updatedReservation => console.log('Reservation updated:', updatedReservation)),
        catchError(this.handleError)
      );
  }

  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => console.log('Reservation deleted:', id)),
        catchError(this.handleError)
      );
  }

  changeStatut(id: number, statut: StatutReservation): Observable<Reservation> {
    return this.http.patch<Reservation>(`${this.apiUrl}/${id}/statut`, null, {
      params: { statut }
    })
      .pipe(
        tap(reservation => console.log('Reservation status updated:', reservation)),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      errorMessage = error.error?.message || `Erreur ${error.status}: ${error.message}`;
    }

    console.error('Reservation Service Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
