// src/environments/environment.development.ts
export const environment = {
  production: false,
  // Option 1: Via Gateway (recommandé)
  apiUrl: 'http://localhost:8081',
  stadiumUrl: 'http://localhost:8083/api/stadiums',
  authUrl: 'http://localhost:8081/auth',

  // Option 2: URLs directes (pour tests sans gateway)
  // authUrl: 'http://localhost:8082/auth',
  // stadiumUrl: 'http://localhost:8083/api/stadiums',
  // reservationUrl: 'http://localhost:8084/api/reservations'
};
