import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { BffService } from './bff.service';

@Injectable({ providedIn: 'root' })
export class VendedorService {
  constructor(private http: HttpClient, private bff: BffService) {}

  private get url() {
    return `${this.bff.url}/api/vendedor`;
  }

  private logError(context: string, error: any): void {
    console.error(`[VendedorService][${context}] Error:`, error);
  }

  getDashboardMetrics(): Observable<any> {
    return this.http.get<any>(`${this.url}/dashboard`).pipe(
      catchError(err => {
        this.logError('getDashboardMetrics', err);
        return throwError(() => err);
      })
    );
  }

  getProductos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/productos`).pipe(
      catchError(err => {
        this.logError('getProductos', err);
        return throwError(() => err);
      })
    );
  }

  crearProducto(producto: any): Observable<any> {
    return this.http.post<any>(`${this.url}/productos`, producto).pipe(
      catchError(err => {
        this.logError('crearProducto', err);
        return throwError(() => err);
      })
    );
  }

  actualizarProducto(id: number, producto: any): Observable<any> {
    return this.http.put<any>(`${this.url}/productos/${id}`, producto).pipe(
      catchError(err => {
        this.logError(`actualizarProducto(${id})`, err);
        return throwError(() => err);
      })
    );
  }

  actualizarEstadoProducto(id: number, estado: string): Observable<any> {
    return this.http.put<any>(`${this.url}/productos/${id}/estado`, { estado }).pipe(
      catchError(err => {
        this.logError(`actualizarEstadoProducto(${id})`, err);
        return throwError(() => err);
      })
    );
  }

  subirImagenes(id: number, formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.url}/productos/${id}/imagenes`, formData).pipe(
      catchError(err => {
        this.logError(`subirImagenes(${id})`, err);
        return throwError(() => err);
      })
    );
  }

  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/pedidos`).pipe(
      catchError(err => {
        this.logError('getPedidos', err);
        return throwError(() => err);
      })
    );
  }

  getEnvios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/envios`).pipe(
      catchError(err => {
        this.logError('getEnvios', err);
        return throwError(() => err);
      })
    );
  }

  actualizarEstadoEnvio(id: number, estado: string): Observable<any> {
    return this.http.put<any>(`${this.url}/envios/${id}/estado`, { estado }).pipe(
      catchError(err => {
        this.logError(`actualizarEstadoEnvio(${id})`, err);
        return throwError(() => err);
      })
    );
  }
}
