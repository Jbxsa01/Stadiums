import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { StadiumService } from '../../services/stadium.service';
import { AuthService } from '../../services/auth.service';
import { Stadium } from '../../models/stadium.model';

@Component({
  selector: 'app-stadium-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stadium-create.component.html',
  styleUrls: ['./stadium-create.component.css']
})
export class StadiumCreateComponent implements OnInit {
  stadium: Stadium = {
    name: '',
    location: '',
    description: '',
    pricePerHour: 0,
    imageUrl: '',
    available: true
  };

  loading = false;
  errorMessage = '';
  successMessage = '';
  isEditMode = false;
  stadiumId?: number;

  constructor(
    private stadiumService: StadiumService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Vérifier que l'utilisateur est admin
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/login']);
      return;
    }

    // Vérifier si on est en mode édition
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.stadiumId = +id;
      this.loadStadium(this.stadiumId);
    }
  }

  loadStadium(id: number): void {
    this.loading = true;
    this.stadiumService.getStadiumById(id).subscribe({
      next: (stadium) => {
        this.stadium = stadium;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading stadium';
        console.error(error);
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const operation = this.isEditMode
      ? this.stadiumService.updateStadium(this.stadiumId!, this.stadium)
      : this.stadiumService.createStadium(this.stadium);

    operation.subscribe({
      next: (response) => {
        console.log('✅ Stadium saved:', response);
        this.successMessage = this.isEditMode
          ? 'Stadium updated successfully!'
          : 'Stadium created successfully!';
        this.loading = false;

        // Rediriger après 1.5 secondes
        setTimeout(() => {
          this.router.navigate(['/stadiums']);
        }, 1500);
      },
      error: (error) => {
        console.error('❌ Error saving stadium:', error);
        this.errorMessage = error.message || 'An error occurred while saving the stadium';
        this.loading = false;
      }
    });
  }

  validateForm(): boolean {
    if (!this.stadium.name || !this.stadium.location || !this.stadium.description) {
      this.errorMessage = 'Please fill in all required fields';
      return false;
    }

    if (this.stadium.pricePerHour <= 0) {
      this.errorMessage = 'Price must be greater than 0';
      return false;
    }

    if (!this.stadium.imageUrl) {
      this.errorMessage = 'Please provide an image URL';
      return false;
    }

    return true;
  }

  goBack(): void {
    this.router.navigate(['/stadiums']);
  }
}
