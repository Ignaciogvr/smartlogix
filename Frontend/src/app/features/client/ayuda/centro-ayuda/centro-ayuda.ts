import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-centro-ayuda',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './centro-ayuda.html',
  styleUrl: './centro-ayuda.css',
})
export class CentroAyuda {
  searchQuery: string = '';

  categorias = [
    { titulo: 'Preguntas frecuentes', icono: '❓' },
    { titulo: 'Compras y CMR Puntos', icono: '💳' },
    { titulo: 'Estado del pedido y entrega', icono: '📦' },
    { titulo: 'Devoluciones y cambios', icono: '🔄' },
    { titulo: 'Garantías y derechos', icono: '🛡️' },
    { titulo: 'Mi cuenta', icono: '👤' },
    { titulo: 'Banco Falabella y tarjeta CMR', icono: '🏦' },
    { titulo: 'Horarios de tiendas y puntos de entrega', icono: '🏪' }
  ];

  masBuscado = [
    { texto: '¿Dónde encuentro la boleta de una compra?', link: '#' },
    { texto: '¿Cómo uso una Gift Card?', link: '#' },
    { texto: '¿Cómo devolver o cambiar un producto?', link: '#' },
    { texto: '¿Dónde reviso el estado de un pedido?', link: '#' }
  ];
}
