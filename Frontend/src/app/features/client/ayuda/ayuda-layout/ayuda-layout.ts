import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-ayuda-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ayuda-layout.html',
  styleUrls: ['./ayuda-layout.css']
})
export class AyudaLayout {
  // Opcional: estado para el menú móvil si fuera necesario
}
