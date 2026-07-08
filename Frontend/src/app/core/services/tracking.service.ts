import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError, of } from 'rxjs';

import { BffService } from './bff.service';

@Injectable({ providedIn: 'root' })
export class TrackingService {

  constructor(
    private http: HttpClient,
    private bff: BffService
  ) {}

  private logError(context: string, error: any): void {
    console.error(`[TrackingService][${context}] Error:`, error);
    if (error?.status === 404) {
      console.warn(`[TrackingService][${context}] Tracking no encontrado.`);
    }
  }

  /** GET /api/tracking/codigo/{code} → tracking por código */
  obtenerPorCodigo(code: string): Observable<any> {
    return this.http.get<any>(
      `${this.bff.url}/api/tracking/codigo/${encodeURIComponent(code)}`
    ).pipe(
      catchError(error => {
        this.logError('obtenerPorCodigo', error);
        return throwError(() => error);
      })
    );
  }

  /** GET /api/tracking/{id} → tracking por ID de envío */
  obtenerPorId(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.bff.url}/api/tracking/${id}`
    ).pipe(
      catchError(error => {
        this.logError(`obtenerPorId(${id})`, error);
        return throwError(() => error);
      })
    );
  }

  /** Alias: obtener por código (compatibilidad) */
  obtener(code: string): Observable<any> {
    return this.obtenerPorCodigo(code);
  }

  obtenerTracking(code: string): Observable<any> {
    return this.obtenerPorCodigo(code);
  }

  /** PUT /api/envios/{id}/estado → actualizar estado (chofer) */
  actualizarEstado(envioId: number, estado: string): Observable<any> {
    return this.http.put<any>(
      `${this.bff.url}/api/envios/${envioId}/estado`,
      { estado }
    ).pipe(
      catchError(error => {
        this.logError('actualizarEstado', error);
        return throwError(() => error);
      })
    );
  }
}
