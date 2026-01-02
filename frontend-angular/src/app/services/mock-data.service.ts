// src/app/services/mock-data.service.ts
import { Injectable } from '@angular/core';
import { Stadium } from '../models/stadium.model';
import { Reservation, StatutReservation } from '../models/reservation.model';

@Injectable({
  providedIn: 'root'
})
export class MockDataService {

  // 🏟️ DONNÉES DE TEST - Stades
  getMockStadiums(): Stadium[] {
    return [
      {
        id: 1,
        name: 'Stade Al Massira',
        location: 'Agadir, Maroc',
        description: 'Terrain synthétique de haute qualité avec éclairage professionnel. Idéal pour matchs nocturnes.',
        pricePerHour: 250,
        imageUrl: 'https://images.unsplash.com/photo-1459865264687-595d652de67e?w=800',
        available: true
      },
      {
        id: 2,
        name: 'Complexe Mohammed V',
        location: 'Casablanca, Maroc',
        description: 'Stade moderne avec vestiaires, douches et espace spectateurs. Parfait pour tournois.',
        pricePerHour: 350,
        imageUrl: 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=800',
        available: true
      },
      {
        id: 3,
        name: 'Arena Sportive Hassan II',
        location: 'Rabat, Maroc',
        description: 'Infrastructure premium avec gazon naturel entretenu quotidiennement.',
        pricePerHour: 450,
        imageUrl: 'https://images.unsplash.com/photo-1522778119026-d647f0596c20?w=800',
        available: true
      },
      {
        id: 4,
        name: 'Stade Al Houda',
        location: 'Marrakech, Maroc',
        description: 'Terrain en plein air avec vue panoramique. Capacité 50 personnes.',
        pricePerHour: 200,
        imageUrl: 'https://images.unsplash.com/photo-1556056504-5c7696c4c28d?w=800',
        available: false
      },
      {
        id: 5,
        name: 'Green Park Stadium',
        location: 'Tanger, Maroc',
        description: 'Complexe sportif avec 3 terrains, parking gratuit et café sur place.',
        pricePerHour: 300,
        imageUrl: 'https://images.unsplash.com/photo-1577223625816-7546f13df25d?w=800',
        available: true
      },
      {
        id: 6,
        name: 'Stade Atlas',
        location: 'Fès, Maroc',
        description: 'Terrain couvert climatisé. Disponible toute l\'année.',
        pricePerHour: 400,
        imageUrl: 'https://images.unsplash.com/photo-1529900748604-07564a03e7a6?w=800',
        available: true
      }
    ];
  }

  // 📅 DONNÉES DE TEST - Réservations
  getMockReservations(): Reservation[] {
    return [
      {
        id: 101,
        cin: 'AB123456',
        date: '2026-01-15',
        heureDebut: '14:00',
        heureFin: '16:00',
        prix: 500,
        statut: StatutReservation.CONFIRMEE,
        stadeId: 1
      },
      {
        id: 102,
        cin: 'CD789012',
        date: '2026-01-20',
        heureDebut: '18:00',
        heureFin: '20:00',
        prix: 700,
        statut: StatutReservation.EN_ATTENTE,
        stadeId: 2
      },
      {
        id: 103,
        cin: 'EF345678',
        date: '2026-01-10',
        heureDebut: '10:00',
        heureFin: '12:00',
        prix: 900,
        statut: StatutReservation.ANNULEE,
        stadeId: 3
      }
    ];
  }
}
