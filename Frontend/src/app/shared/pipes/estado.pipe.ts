import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'estadoPedido',
  standalone: true
})
export class EstadoPipe implements PipeTransform {

  transform(estado: string | null | undefined): string {

    if (!estado) {
      return 'Sin estado';
    }

    const estados: Record<string, string> = {
      PENDIENTE: 'Pendiente',
      PAGADO: 'Pagado',
      PREPARANDO: 'Preparando',
      ENVIADO: 'Enviado',
      ENTREGADO: 'Entregado',
      CANCELADO: 'Cancelado',
      DEVUELTO: 'Devuelto'
    };

    return estados[estado.toUpperCase()] || estado;
  }
}