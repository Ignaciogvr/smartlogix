import { Component, EventEmitter, Input, Output } from '@angular/core';

import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.html',
  styleUrls: ['./search-bar.css']
})
export class SearchBarComponent {

  termino = '';

  @Input() placeholder = 'Buscar productos...';

  @Output()
  buscar = new EventEmitter<string>();

  onBuscar(): void {
    this.buscar.emit(this.termino.trim());
  }

  onInput(): void {
    this.buscar.emit(this.termino.trim());
  }

  limpiar(): void {
    this.termino = '';
    this.buscar.emit('');
  }
}