import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, map, throwError, of } from 'rxjs';

import { BffService } from './bff.service';


@Injectable({
  providedIn: 'root'
})
export class PedidoService {

  constructor(
    private http: HttpClient,
    private bff: BffService
  ) {}

  private get base(): string {
    return `${this.bff.url}/api/pedidos`;
  }

  private logError(context: string, error: any): void {
    console.error(`[PedidoService][${context}] Error:`, error);
    if (error?.status === 0) {
      console.warn(`[PedidoService][${context}] Sin conexión al servidor.`);
    } else if (error?.status === 401 || error?.status === 403) {
      console.warn(`[PedidoService][${context}] Error de autorización (${error.status}).`);
    } else if (error?.status >= 500) {
      console.warn(`[PedidoService][${context}] Error de servidor (${error.status}).`);
    }
  }

  // ─── Mis Pedidos ───────────────────────────────────────────

  /** GET /api/pedidos/mis-pedidos → pedidos del usuario autenticado */
  listar(): Observable<any[]> {
    return this.http.get<any>(`${this.base}/mis-pedidos`).pipe(
      map(response => {
        if (Array.isArray(response)) return response;
        if (Array.isArray(response?.data)) return response.data;
        if (Array.isArray(response?.pedidos)) return response.pedidos;
        return [];
      }),
      catchError(error => {
        this.logError('listar', error);
        return throwError(() => error);
      })
    );
  }

  /** Alias: compatibilidad con PedidosComponent */
  pedidosUsuario(_usuarioId?: string): Observable<any[]> {
    return this.listar();
  }

  /** GET /api/pedidos/{id} → detalle de un pedido */
  obtenerPedido(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/${id}`).pipe(
      catchError(error => {
        this.logError(`obtenerPedido(${id})`, error);
        return throwError(() => error);
      })
    );
  }

  /** GET /api/v1/pedidos/{id}/estado-completo → estado completo con envío */
  estadoPedidoCompleto(pedidoId: number): Observable<any> {
    return this.http.get<any>(
      `${this.bff.url}/api/v1/pedidos/${pedidoId}/estado-completo`
    ).pipe(
      catchError(error => {
        this.logError(`estadoPedidoCompleto(${pedidoId})`, error);
        return throwError(() => error);
      })
    );
  }

  // ─── Checkout ─────────────────────────────────────────────

  /** POST /api/checkout/confirmar → confirmar compra */
  checkout(payload: any, idempotencyKey?: string): Observable<any> {
    let headers = new HttpHeaders();
    if (idempotencyKey) {
      headers = headers.set('Idempotency-Key', idempotencyKey);
    }

    return this.http.post<any>(
      `${this.bff.url}/api/checkout/confirmar`,
      payload,
      { headers }
    ).pipe(
      catchError(error => {
        this.logError('checkout', error);
        return throwError(() => error);
      })
    );
  }

  /** POST /api/checkout → alias del checkout (endpoint principal) */
  crearPedido(payload: any): Observable<any> {
    return this.checkout(payload);
  }

  // ─── Cancelación ──────────────────────────────────────────

  /** PUT /api/admin/pedidos/{id}/cancelar → cancelar pedido */
  cancelarPedido(pedidoId: number): Observable<any> {
    return this.http.put<any>(
      `${this.base}/${pedidoId}/cancelar`,
      {}
    ).pipe(
      catchError(error => {
        this.logError('cancelarPedido', error);
        return throwError(() => error);
      })
    );
  }

  // ─── Métodos legacy (compatibilidad) ──────────────────────

  usuario(_usuarioId: string): Observable<any[]> {
    return this.listar();
  }

  /** @deprecated Usar AdminService.preparar/enviarPedido en su lugar */
  actualizarEstado(pedidoId: number, estado: string): Observable<any> {
    const accion = estado.toLowerCase();
    return this.http.put<any>(
      `${this.bff.url}/api/admin/pedidos/${pedidoId}/${accion}`,
      {}
    ).pipe(
      catchError(error => {
        this.logError('actualizarEstado', error);
        return throwError(() => error);
      })
    );
  }
}
