// account-no-mask.directive.ts
import { Directive, HostListener, ElementRef } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[accountMask]'
})
export class AccountMaskDirective {
  private readonly maxDigits = 12;

  constructor(private el: ElementRef<HTMLInputElement>, private ngControl: NgControl) {}

  @HostListener('input', ['$event'])
  onInput(e: Event) {
    const input = this.el.nativeElement;

    let digits = (input.value || '').replace(/\D/g, '').slice(0, this.maxDigits);

    // Build formatted string: XXXX-XXXX-XXXX
    const groups = digits.match(/.{1,4}/g) || [];
    const formatted = groups.join('-');

    // Set the display value (with dashes)
    const cursorFromEnd = input.value.length - input.selectionStart!;
    input.value = formatted;
  }

  // Handle paste
  @HostListener('paste', ['$event'])
  onPaste(e: ClipboardEvent) {
    e.preventDefault();
    const text = (e.clipboardData?.getData('text') || '').replace(/\D/g, '').slice(0, this.maxDigits);
    const groups = text.match(/.{1,4}/g) || [];
    const formatted = groups.join('-');
    this.el.nativeElement.value = formatted;
  }
}