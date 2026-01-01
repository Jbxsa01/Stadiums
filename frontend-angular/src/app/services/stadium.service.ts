// src/app/services/stadium.service.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Stadium } from '../models/stadium.model';
import { environment } from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class StadiumService {
  // ✅ URL corrigée
  private apiUrl = environment.stadiumUrl;

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {
    console.log('🔗 Stadium API URL:', this.apiUrl); // ✅ Debug
  }

  getAllStadiums(): Observable<Stadium[]> {
    console.log('📡 Fetching stadiums from:', this.apiUrl);
    return this.http.get<Stadium[]>(this.apiUrl)
      .pipe(
        tap(stadiums => console.log('✅ Stadiums retrieved:', stadiums.length)),
        catchError(this.handleError)
      );
  }

  getStadiumById(id: number): Observable<Stadium> {
    return this.http.get<Stadium>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(stadium => console.log('✅ Stadium retrieved:', stadium)),
        catchError(this.handleError)
      );
  }

  createStadium(stadium: Stadium): Observable<Stadium> {
    return this.http.post<Stadium>(this.apiUrl, stadium)
      .pipe(
        tap(newStadium => console.log('✅ Stadium created:', newStadium)),
        catchError(this.handleError)
      );
  }

  updateStadium(id: number, stadium: Stadium): Observable<Stadium> {
    return this.http.put<Stadium>(`${this.apiUrl}/${id}`, stadium)
      .pipe(
        tap(updatedStadium => console.log('✅ Stadium updated:', updatedStadium)),
        catchError(this.handleError)
      );
  }

  deleteStadium(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => console.log('✅ Stadium deleted:', id)),
        catchError(this.handleError)
      );
  }

  private handleError = (error: HttpErrorResponse) => {
    let errorMessage = 'Une erreur est survenue';

    console.error('❌ Stadium Service Error:', error); // ✅ Debug complet

    if (isPlatformBrowser(this.platformId) && error.error instanceof ErrorEvent) {
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      errorMessage = error.error?.message || `Erreur ${error.status}: ${error.message}`;
    }

    console.error('Stadium Service Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
