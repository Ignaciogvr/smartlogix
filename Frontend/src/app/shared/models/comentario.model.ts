export interface Comentario {
  id: number;
  usuarioId: string;
  nombreCliente: string;
  fecha: string; // ISO date string
  fechaActualizacion?: string;
  calificacion: number;
  comentario: string;
  compraVerificada: boolean;
  respuestaEmpresa?: string;
}

export interface ComentarioRequest {
  calificacion: number;
  comentario: string;
}
