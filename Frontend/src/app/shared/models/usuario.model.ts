export interface Usuario {

  id?: number;

  auth0Id: string;

  nombre: string;

  apellido: string;

  email: string;

  telefono?: string;

  direccion?: string;

  rol:
    | 'ADMIN'
    | 'CLIENTE'
    | 'VENTAS'
    | 'LOGISTICA';

  activo: boolean;
}