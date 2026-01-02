// src/app/services/auth.service.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError, of } from 'rxjs';
import { tap, catchError, delay } from 'rxjs/operators';
import { RegisterRequest, LoginRequest, AuthResponse } from '../models/user.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = (environment as any).authUrl ?? `${environment.apiUrl}/auth`;
  private currentUserSubject: BehaviorSubject<AuthResponse | null>;
  public currentUser: Observable<AuthResponse | null>;

  // 🔥 MODE MOCK pour la soutenance
  private useMockAuth = true;

  // 👤 Utilisateurs mock (pour démo)
  private mockUsers = [
    {
      userId: 1,
      username: 'admin',
      email: 'admin@footreserve.com',
      password: 'admin123',
      role: 'ADMIN'
    },
    {
      userId: 2,
      username: 'user',
      email: 'user@test.com',
      password: 'user123',
      role: 'USER'
    }
  ];

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    let storedUser: string | null = null;
    if (isPlatformBrowser(this.platformId)) {
      storedUser = localStorage.getItem('currentUser');
    }
    this.currentUserSubject = new BehaviorSubject<AuthResponse | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    if (this.useMockAuth) {
      console.log('📦 MOCK: Registration attempt', request);

      // Vérifier si l'utilisateur existe déjà
      const userExists = this.mockUsers.find(u =>
        u.username === request.username || u.email === request.email
      );

      if (userExists) {
        return throwError(() => new Error('Nom d\'utilisateur ou email déjà utilisé'));
      }

      // Créer un nouvel utilisateur
      const newUser = {
        userId: this.mockUsers.length + 1,
        username: request.username,
        email: request.email,
        password: request.password,
        role: 'USER'
      };

      this.mockUsers.push(newUser);

      const response: AuthResponse = {
        userId: newUser.userId,
        username: newUser.username,
        email: newUser.email,
        role: newUser.role,
        message: 'Inscription réussie'
      };

      return of(response).pipe(delay(500));
    }

    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request)
      .pipe(
        tap(response => {
          console.log('✅ Registration successful:', response);
        }),
        catchError(this.handleError)
      );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    if (this.useMockAuth) {
      console.log('📦 MOCK: Login attempt', request);

      // Chercher l'utilisateur dans les données mock
      const user = this.mockUsers.find(u =>
        u.username === request.username && u.password === request.password
      );

      if (!user) {
        return throwError(() => new Error('Nom d\'utilisateur ou mot de passe incorrect'));
      }

      const response: AuthResponse = {
        userId: user.userId,
        username: user.username,
        email: user.email,
        role: user.role,
        message: 'Connexion réussie'
      };

      // Stocker dans localStorage
      if (isPlatformBrowser(this.platformId)) {
        localStorage.setItem('currentUser', JSON.stringify(response));
      }
      this.currentUserSubject.next(response);

      console.log(`✅ Login successful as ${user.role}:`, response);
      return of(response).pipe(delay(500));
    }

    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request)
      .pipe(
        tap(response => {
          if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem('currentUser', JSON.stringify(response));
          }
          this.currentUserSubject.next(response);
          console.log('✅ Login successful:', response);
        }),
        catchError(this.handleError)
      );
  }

  logout(userId?: number): Observable<string> {
    if (this.useMockAuth) {
      console.log('📦 MOCK: Logout');
      if (isPlatformBrowser(this.platformId)) {
        localStorage.removeItem('currentUser');
      }
      this.currentUserSubject.next(null);
      return of('Déconnexion réussie').pipe(delay(200));
    }

    return this.http.post<string>(`${this.apiUrl}/logout/${userId}`, {})
      .pipe(
        tap(() => {
          if (isPlatformBrowser(this.platformId)) {
            localStorage.removeItem('currentUser');
          }
          this.currentUserSubject.next(null);
          console.log('✅ Logout successful');
        }),
        catchError(this.handleError)
      );
  }

  isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }

  isAdmin(): boolean {
    return this.currentUserValue?.role === 'ADMIN';
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      if (error.error && typeof error.error === 'object') {
        errorMessage = error.error.error || error.error.message || errorMessage;
      } else if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else {
        errorMessage = `Erreur ${error.status}: ${error.message}`;
      }
    }

    console.error('❌ Auth Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
