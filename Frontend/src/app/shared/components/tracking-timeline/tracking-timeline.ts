import {
  Component,
  Input,
  OnChanges,
  SimpleChanges
} from '@angular/core';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tracking-timeline',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tracking-timeline.html',
  styleUrls: ['./tracking-timeline.css']
})
export class TrackingTimelineComponent
  implements OnChanges {

  @Input()
  estadoActual = '';

  timeline = [
    {
      estado: 'PENDIENTE',
      titulo: 'Pedido Recibido',
      descripcion: 'Tu pedido fue recibido.',
      completado: false
    },
    {
      estado: 'PREPARANDO',
      titulo: 'Preparando Pedido',
      descripcion: 'Estamos preparando tu compra.',
      completado: false
    },
    {
      estado: 'EN_CAMINO',
      titulo: 'En Camino',
      descripcion: 'Tu pedido va rumbo a destino.',
      completado: false
    },
    {
      estado: 'ENTREGADO',
      titulo: 'Pedido Entregado',
      descripcion: 'Tu pedido fue entregado.',
      completado: false
    }
  ];

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    if (changes['estadoActual']) {
      this.marcarEstados();
    }
  }

  marcarEstados(): void {

    const indexActual =
      this.timeline.findIndex(
        item =>
          item.estado === this.estadoActual
      );

    this.timeline =
      this.timeline.map((item, index) => ({
        ...item,
        completado:
          index <= indexActual
      }));
  }
}