import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'inscription',
    loadChildren: () =>
      import('./inscription/inscription.module').then((m) => m.InscriptionModule),
  },
  { path: '', redirectTo: 'inscription', pathMatch: 'full' },
];
