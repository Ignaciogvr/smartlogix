import { Producto } from './producto.model';

export interface Pedido {

  id: number;

  usuarioId: string;

  productos: Producto[];

  total: number;

  estado:
    | 'PENDIENTE'
    | 'PAGADO'
    | 'PREPARANDO'
    | 'EN_CAMINO'
    | 'ENTREGADO'
    | 'CANCELADO';

  fechaCreacion: string;
}