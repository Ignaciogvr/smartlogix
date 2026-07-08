import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError, of } from 'rxjs';
import { BffService } from './bff.service';

@Injectable({ providedIn: 'root' })
export class AdminService {

  constructor(
    private http: HttpClient,
    private bff: BffService
  ) {}

  private get base(): string {
    return `${this.bff.url}/api/admin`;
  }

  // ─── Dashboard ────────────────────────────────────────────

  /** GET /api/admin/dashboard → métricas del dashboard admin */
  dashboard(): Observable<any> {
    return this.http.get<any>(`${this.base}/dashboard`).pipe(
      catchError(err => {
        console.error('[AdminService][dashboard] Error:', err);
        return throwError(() => err);
      })
    );
  }

  dashboardAdmin(): Observable<any> {
    return this.dashboard();
  }

  // ─── Pedidos Admin ────────────────────────────────────────

  /** GET /api/admin/pedidos → todos los pedidos */
  listarTodosPedidos(): Observable<any[]> {
    return this.http.get<any>(`${this.base}/pedidos`).pipe(
      catchError(err => {
        console.error('[AdminService][listarTodosPedidos] Error:', err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/admin/pedidos/estado/{estado} → pedidos filtrados por estado */
  pedidosPorEstado(estado: string): Observable<any[]> {
    return this.http.get<any>(`${this.base}/pedidos/estado/${estado}`).pipe(
      catchError(err => {
        console.error(`[AdminService][pedidosPorEstado(${estado})] Error:`, err);
        return throwError(() => err);
      })
    );
  }

  /** PUT /api/admin/pedidos/{id}/preparar → cambiar estado a PREPARANDO */
  preparar(pedidoId: number): Observable<any> {
    return this.http.put<any>(`${this.base}/pedidos/${pedidoId}/preparar`, {}).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /** PUT /api/admin/pedidos/{id}/enviar → cambiar estado a ENVIADO */
  enviarPedido(pedidoId: number): Observable<any> {
    return this.http.put<any>(`${this.base}/pedidos/${pedidoId}/enviar`, {}).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /** PUT /api/admin/pedidos/{id}/cancelar → cancelar pedido */
  cancelarPedido(pedidoId: number): Observable<any> {
    return this.http.put<any>(`${this.bff.url}/api/pedidos/${pedidoId}/cancelar`, {}).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /** @deprecated No existe endpoint de entregar en AdminBffController */
  entregarPedido(pedidoId: number): Observable<any> {
    // El BFF no tiene /entregar, usar /enviar como último paso
    return this.enviarPedido(pedidoId);
  }

  // ─── Envíos Admin ─────────────────────────────────────────

  /** GET /api/admin/envios → todos los envíos */
  listarTodosEnvios(): Observable<any[]> {
    return this.http.get<any>(`${this.base}/envios`).pipe(
      catchError(err => {
        console.error('[AdminService][listarTodosEnvios] Error:', err);
        return throwError(() => err);
      })
    );
  }

  /** PUT /api/envios/{id}/estado → actualizar estado de un envío */
  actualizarEstadoEnvio(envioId: number, estado: string): Observable<any> {
    return this.http.put<any>(
      `${this.bff.url}/api/envios/${envioId}/estado`,
      { estado }
    ).pipe(
      catchError(err => throwError(() => err))
    );
  }

  // ─── Usuarios Admin ───────────────────────────────────────

  /** GET /api/usuarios → listar todos los usuarios */
  listarUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.bff.url}/api/usuarios`).pipe(
      catchError(err => {
        console.error('[AdminService][listarUsuarios] Error:', err);
        return throwError(() => err);
      })
    );
  }

  // ─── Productos - via Vendedor (admin puede también) ───────

  crearProducto(producto: any): Observable<any> {
    return this.http.post<any>(`${this.bff.url}/api/vendedor/productos`, producto).pipe(
      catchError(err => throwError(() => err))
    );
  }

  actualizarProducto(id: number, producto: any): Observable<any> {
    return this.http.put<any>(`${this.bff.url}/api/vendedor/productos/${id}`, producto).pipe(
      catchError(err => throwError(() => err))
    );
  }

  eliminarProducto(id: number): Observable<any> {
    return this.http.delete<any>(`${this.bff.url}/api/vendedor/productos/${id}`).pipe(
      catchError(err => throwError(() => err))
    );
  }
}