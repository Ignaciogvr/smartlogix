export interface Envio {

  id: number;

  pedidoId: number;

  usuarioId: string;

  trackingCode: string;

  direccionEntrega: string;

  estado:
    | 'PENDIENTE'
    | 'PREPARANDO'
    | 'EN_CAMINO'
    | 'ENTREGADO';

  fechaEnvio?: string;

  fechaEntrega?: string;
}