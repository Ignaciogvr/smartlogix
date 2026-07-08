export interface Tracking {

  trackingCode: string;

  pedidoId: number;

  estado:
    | 'PENDIENTE'
    | 'PREPARANDO'
    | 'EN_CAMINO'
    | 'ENTREGADO';

  ubicacionActual?: string;

  fechaActualizacion?: string;

  detalle?: string;
}