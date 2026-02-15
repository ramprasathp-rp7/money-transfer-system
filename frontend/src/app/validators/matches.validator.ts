import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function notEqual(expected: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    return value !== expected ? null : { notEqual: true }
  };
}