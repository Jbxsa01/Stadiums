import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Stadium } from '../models/stadium.model';
import { MockDataService } from './mock-data.service';
import { environment } from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class StadiumService {
  private apiUrl = environment.stadiumUrl;
  private useMockData = true; // 🔧 Basculer à false quand le backend est prêt

  constructor(
    private http: HttpClient,
    private mockDataService: MockDataService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    console.log('🔗 Stadium API URL:', this.apiUrl);
  }

  getAllStadiums(): Observable<Stadium[]> {
    // Si mode mock activé, retourner les données de test
    if (this.useMockData) {
      console.log('📦 Using MOCK data for stadiums');
      return of(this.mockDataService.getMockStadiums());
    }

    // Sinon, appeler l'API réelle
    console.log('📡 Fetching stadiums from:', this.apiUrl);
    return this.http.get<Stadium[]>(this.apiUrl)
      .pipe(
        tap(stadiums => console.log('✅ Stadiums retrieved:', stadiums.length)),
        catchError((error) => {
          console.warn('⚠️ API failed, falling back to mock data');
          return of(this.mockDataService.getMockStadiums());
        })
      );
  }

  getStadiumById(id: number): Observable<Stadium> {
    if (this.useMockData) {
      const stadium = this.mockDataService.getMockStadiums().find(s => s.id === id);
      return stadium ? of(stadium) : throwError(() => new Error('Stadium not found'));
    }

    return this.http.get<Stadium>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(stadium => console.log('✅ Stadium retrieved:', stadium)),
        catchError(this.handleError)
      );
  }

  createStadium(stadium: Stadium): Observable<Stadium> {
    if (this.useMockData) {
      console.log('📦 MOCK: Stadium created', stadium);
      // Simuler la création avec un ID généré
      const newStadium = { ...stadium, id: Date.now() };
      return of(newStadium);
    }

    return this.http.post<Stadium>(this.apiUrl, stadium)
      .pipe(
        tap(newStadium => console.log('✅ Stadium created:', newStadium)),
        catchError(this.handleError)
      );
  }

  updateStadium(id: number, stadium: Stadium): Observable<Stadium> {
    if (this.useMockData) {
      console.log('📦 MOCK: Stadium updated', stadium);
      return of({ ...stadium, id });
    }

    return this.http.put<Stadium>(`${this.apiUrl}/${id}`, stadium)
      .pipe(
        tap(updatedStadium => console.log('✅ Stadium updated:', updatedStadium)),
        catchError(this.handleError)
      );
  }

  deleteStadium(id: number): Observable<void> {
    if (this.useMockData) {
      console.log('📦 MOCK: Stadium deleted', id);
      return of(undefined);
    }

    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => console.log('✅ Stadium deleted:', id)),
        catchError(this.handleError)
      );
  }

  private handleError = (error: HttpErrorResponse) => {
    let errorMessage = 'Une erreur est survenue';

    console.error('❌ Stadium Service Error:', error);

    if (isPlatformBrowser(this.platformId) && error.error instanceof ErrorEvent) {
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      errorMessage = error.error?.message || `Erreur ${error.status}: ${error.message}`;
    }

    return throwError(() => new Error(errorMessage));
  }
}
