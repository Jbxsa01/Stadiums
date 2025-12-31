
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stadium-form',
  templateUrl: './stadium-form.html',
  styleUrls: ['./stadium-form.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class StadiumForm {
  stadiumForm: FormGroup;
  constructor(private fb: FormBuilder) {
    this.stadiumForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      pricePerHour: [null, [Validators.required, Validators.min(0)]],
      description: ['', [Validators.maxLength(500)]],
      imageUrl: ['', [Validators.pattern('https?://.+')]],
      location: ['', [Validators.required, Validators.maxLength(200)]],
      available: [true, Validators.required]
    });
  }

  get f() { return this.stadiumForm.controls; }

  onSubmit() {
    if (this.stadiumForm.valid) {
      // TODO: Envoyer les données au backend
      alert('Stadium enregistré!\n' + JSON.stringify(this.stadiumForm.value, null, 2));
    } else {
      this.stadiumForm.markAllAsTouched();
    }
  }
}
