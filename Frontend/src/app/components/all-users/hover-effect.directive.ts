import { Directive, HostBinding, HostListener } from '@angular/core';

//Dodato zbog efekta
@Directive({
  selector: '[appHoverEffect]'
})
export class HoverEffectDirective {
  @HostBinding('style.backgroundColor') backgroundColor!: string;

  @HostListener('mouseenter') onMouseEnter() {
    this.backgroundColor = 'lightcyan';
  }

  @HostListener('mouseleave') onMouseLeave() {
    this.backgroundColor = '';
  }
}
