import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class StadiumFormService {
  private _form: FormGroup | null = null;

  setForm(form: FormGroup) {
    this._form = form;
  }

  getForm(): FormGroup | null {
    return this._form;
  }
}
