// src/app/app.routes.ts
import { Routes } from '@angular/router';

// Auth
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

// Stadium
import { StadiumListComponent } from './components/stadium-list/stadium-list.component';

// Reservation
import { ReservationCreateComponent } from './components/reservation-create/reservation-create.component';

export const routes: Routes = [
  // Redirection par défaut
  { path: '', redirectTo: '/stadiums', pathMatch: 'full' },

  // Authentification
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // Stades
  { path: 'stadiums', component: StadiumListComponent },

  // Réservation (par stade)
  { path: 'reservation/:id', component: ReservationCreateComponent },

  // Autres routes possibles (à venir)
  // { path: 'mes-reservations', component: MyReservationsComponent },
  // { path: 'admin', component: AdminDashboardComponent },

  // Route wildcard
  { path: '**', redirectTo: '/stadiums' }
];
