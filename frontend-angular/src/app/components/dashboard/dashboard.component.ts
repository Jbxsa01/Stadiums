import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MockDataService } from '../../services/mock-data.service';
import { Stadium } from '../../models/stadium.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  activeBookings = 3;
  featuredStadiums: Stadium[] = [];

  constructor(
    private router: Router,
    private authService: AuthService,
    private mockDataService: MockDataService
  ) {}

  ngOnInit(): void {
    // Charger les 3 premiers stades comme "featured"
    const allStadiums = this.mockDataService.getMockStadiums();
    this.featuredStadiums = allStadiums.slice(0, 3);
  }

  get currentUser() {
    return this.authService.currentUserValue;
  }

  goToStadiums() {
    this.router.navigate(['/stadiums']);
  }

  goToBookings() {
    this.router.navigate(['/my-bookings']);
  }
}
