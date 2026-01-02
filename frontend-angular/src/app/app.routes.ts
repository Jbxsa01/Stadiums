// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { StadiumListComponent } from './components/stadium-list/stadium-list.component';
import { ReservationCreateComponent } from './components/reservation-create/reservation-create.component';
import { MyBookingsComponent } from './components/my-bookings/my-bookings.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { StadiumCreateComponent } from './components/stadium-create/stadium-create.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'stadiums', component: StadiumListComponent },
  { path: 'reservation/:id', component: ReservationCreateComponent },
  { path: 'my-bookings', component: MyBookingsComponent },

  // Routes Admin
  { path: 'admin/dashboard', component: AdminDashboardComponent },
  { path: 'admin/reservations', component: AdminDashboardComponent }, // Même composant
  { path: 'admin/stadium/create', component: StadiumCreateComponent },
  { path: 'admin/stadium/edit/:id', component: StadiumCreateComponent },

  { path: '**', redirectTo: '/stadiums' }
];
