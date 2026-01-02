// src/app/services/reservation.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Reservation, StatutReservation } from '../models/reservation.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private apiUrl = environment.reservationUrl;

  constructor(private http: HttpClient) {}

  createReservation(reservation: Reservation, cinFile: File): Observable<Reservation> {
    const formData = new FormData();

    formData.append('cinFile', cinFile, cinFile.name);
    formData.append(
      'reservation',
      new Blob([JSON.stringify(reservation)], { type: 'application/json' })
    );

    return this.http.post<Reservation>(this.apiUrl, formData).pipe(
      tap(res => console.log('✅ Reservation created:', res)),
      catchError(this.handleError)
    );
  }

  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.apiUrl).pipe(
      tap(res => console.log('✅ Reservations retrieved:', res.length)),
      catchError(this.handleError)
    );
  }

  getReservationById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`).pipe(
      tap(res => console.log('✅ Reservation retrieved:', res)),
      catchError(this.handleError)
    );
  }

  updateReservation(id: number, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}`, reservation).pipe(
      tap(res => console.log('✅ Reservation updated:', res)),
      catchError(this.handleError)
    );
  }

  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('✅ Reservation deleted:', id)),
      catchError(this.handleError)
    );
  }

  changeStatut(id: number, statut: StatutReservation): Observable<Reservation> {
    return this.http.patch<Reservation>(
      `${this.apiUrl}/${id}/statut`,
      null,
      { params: { statut } }
    ).pipe(
      tap(res => console.log('✅ Reservation status updated:', res)),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erreur client : ${error.error.message}`;
    } else {
      errorMessage = error.error?.message || `Erreur ${error.status} : ${error.message}`;
    }

    console.error('❌ Reservation Service Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
