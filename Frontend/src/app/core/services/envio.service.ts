import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError, of } from 'rxjs';
import { BffService } from './bff.service';

@Injectable({ providedIn: 'root' })
export class EnvioService {

  constructor(private http: HttpClient, private bff: BffService) {}

  private get base(): string {
    return `${this.bff.url}/api/envios`;
  }

  private logError(context: string, error: any): void {
    console.error(`[EnvioService][${context}] Error:`, error);
  }

  // ─── Listados ──────────────────────────────────────────────

  /** GET /api/envios → listar todos los envíos (admin) */
  listar(): Observable<any> {
    return this.http.get<any>(this.base).pipe(
      catchError(err => {
        this.logError('listar', err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/envios/mis-envios → mis envíos (usuario autenticado) */
  misEnvios(): Observable<any[]> {
    return this.http.get<any>(`${this.base}/mis-envios`).pipe(
      catchError(err => {
        this.logError('misEnvios', err);
        return of([]);
      })
    );
  }

  /** GET /api/envios/{id} → obtener envío por ID */
  obtener(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/${id}`).pipe(
      catchError(err => {
        this.logError(`obtener(${id})`, err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/envios/usuario/{id} → envíos de un usuario específico */
  porUsuario(usuarioId: string): Observable<any> {
    return this.http.get<any>(`${this.base}/usuario/${usuarioId}`).pipe(
      catchError(err => {
        this.logError('porUsuario', err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/envios/pedido/{id} → envío de un pedido */
  obtenerPorPedido(pedidoId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/pedido/${pedidoId}`).pipe(
      catchError(err => {
        this.logError('obtenerPorPedido', err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/envios/{id}/seguimiento → seguimiento detallado */
  seguimiento(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/${id}/seguimiento`).pipe(
      catchError(err => {
        this.logError(`seguimiento(${id})`, err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/envios/tracking/{code} → tracking por código */
  obtenerPorTracking(trackingCode: string): Observable<any> {
    return this.http.get<any>(
      `${this.base}/tracking/${encodeURIComponent(trackingCode)}`
    ).pipe(
      catchError(err => {
        this.logError('obtenerPorTracking', err);
        return throwError(() => err);
      })
    );
  }

  /** GET /api/envios/cotizar?region=RM&peso=1 → cotizar envío */
  cotizar(region: string, peso: number): Observable<any> {
    return this.http.get<any>(`${this.base}/cotizar`, {
      params: new HttpParams().set('region', region).set('peso', peso)
    }).pipe(
      catchError(err => {
        this.logError('cotizar', err);
        return throwError(() => err);
      })
    );
  }

  // ─── Operaciones ───────────────────────────────────────────

  /** POST /api/envios → crear envío */
  crearEnvio(data: {
    pedidoId: number;
    direccionEnvio?: string;
    ciudadEnvio?: string;
  }): Observable<any> {
    return this.http.post<any>(this.base, data).pipe(
      catchError(err => {
        this.logError('crearEnvio', err);
        return throwError(() => err);
      })
    );
  }

  /** PUT /api/envios/{id}/estado → actualizar estado del envío */
  actualizarEstado(envioId: number, estado: string): Observable<any> {
    return this.http.put<any>(`${this.base}/${envioId}/estado`, { estado }).pipe(
      catchError(err => {
        this.logError('actualizarEstado', err);
        return throwError(() => err);
      })
    );
  }

  /** PUT /api/envios/{id}/asignar-chofer → asignar chofer */
  asignarChofer(envioId: number, data: {
    choferId: string;
    choferNombre?: string;
  }): Observable<any> {
    return this.http.put<any>(`${this.base}/${envioId}/asignar-chofer`, data).pipe(
      catchError(err => {
        this.logError('asignarChofer', err);
        return throwError(() => err);
      })
    );
  }

  // ─── Métodos legacy ───────────────────────────────────────

  /** GET /chofer/envios → envíos asignados al chofer autenticado */
  choferEnvios(): Observable<any[]> {
    return this.http.get<any>(`${this.bff.url}/api/chofer/envios`).pipe(
      catchError(err => {
        this.logError('choferEnvios', err);
        // Fallback a mis-envios si el endpoint de chofer no responde
        return this.misEnvios();
      })
    );
  }

  /** PUT /chofer/envios/{id}/estado → actualizar estado (chofer) */
  choferActualizarEstado(envioId: number, estado: string): Observable<any> {
    return this.http.put<any>(
      `${this.bff.url}/api/chofer/envios/${envioId}/estado`,
      { estado }
    ).pipe(
      catchError(err => {
        this.logError('choferActualizarEstado', err);
        return throwError(() => err);
      })
    );
  }

  /** @deprecated Usar choferEnvios() */
  obtenerPorChofer(_choferId: string): Observable<any> {
    return this.choferEnvios();
  }
}