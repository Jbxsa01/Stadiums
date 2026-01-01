// src/app/models/reservation.model.ts
export enum StatutReservation {
  EN_ATTENTE = 'EN_ATTENTE',
  CONFIRMEE = 'CONFIRMEE',
  ANNULEE = 'ANNULEE'
}

export interface Reservation {
  id?: number;
  cin: string;
  date: string; // Format: YYYY-MM-DD
  heureDebut: string; // Format: HH:mm
  heureFin: string; // Format: HH:mm
  prix?: number;
  statut?: StatutReservation;
  ticketPdfPath?: string;
  cinUploadPath?: string;
  stadeId: number;
}

export interface ReservationCreateRequest {
  reservation: Reservation;
  cinFile: File;
}
