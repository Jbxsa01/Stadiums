// src/app/models/stadium.model.ts
export interface Stadium {
  id?: number;
  name: string;
  pricePerHour: number;
  description: string;
  imageUrl: string;
  location: string;
  available: boolean;
}
