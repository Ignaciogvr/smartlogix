import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of } from 'rxjs';
import { BffService } from './bff.service';
import { Producto } from '../../shared/models/producto.model';
import { normalizarProductos } from '../../shared/utils/producto.util';

export interface HomeData {
  destacados: Producto[];
  ofertas: Producto[];
  nuevos: Producto[];
  banners: any[];
  resumen?: any;
}

@Injectable({ providedIn: 'root' })
export class HomeService {

  constructor(
    private http: HttpClient,
    private bff: BffService
  ) {}

  private get base(): string {
    return `${this.bff.url}/api/home`;
  }

  /**
   * GET /api/home → datos completos de la home page
   * Devuelve todo en una sola petición: destacados, ofertas, nuevos, banners
   */
  getDatosHome(): Observable<HomeData> {
    return this.http.get<any>(this.base).pipe(
      map(response => this.mapearHomeData(response)),
      catchError(err => {
        console.error('[HomeService][getDatosHome] Error:', err);
        return of({
          destacados: [],
          ofertas: [],
          nuevos: [],
          banners: [],
          resumen: null
        });
      })
    );
  }

  /** GET /api/home/destacados → productos destacados */
  getDestacados(): Observable<Producto[]> {
    return this.http.get<any[]>(`${this.base}/destacados`).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : [])),
      catchError(err => {
        console.error('[HomeService][getDestacados] Error:', err);
        return of([]);
      })
    );
  }

  /** GET /api/home/ofertas → productos en oferta */
  getOfertas(): Observable<Producto[]> {
    return this.http.get<any[]>(`${this.base}/ofertas`).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : [])),
      catchError(err => {
        console.error('[HomeService][getOfertas] Error:', err);
        return of([]);
      })
    );
  }

  /** GET /api/home/nuevos → productos nuevos */
  getNuevos(): Observable<Producto[]> {
    return this.http.get<any[]>(`${this.base}/nuevos`).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : [])),
      catchError(err => {
        console.error('[HomeService][getNuevos] Error:', err);
        return of([]);
      })
    );
  }

  /** GET /api/home/banners → banners activos */
  getBanners(): Observable<any[]> {
    return this.http.get<any>(`${this.base}/banners`).pipe(
      map(response => {
        if (Array.isArray(response)) return response;
        if (Array.isArray(response?.data)) return response.data;
        if (Array.isArray(response?.banners)) return response.banners;
        return [];
      }),
      catchError(err => {
        console.error('[HomeService][getBanners] Error:', err);
        return of([]);
      })
    );
  }

  /** GET /api/home/resumen → resumen de estadísticas del catálogo */
  getResumen(): Observable<any> {
    return this.http.get<any>(`${this.base}/resumen`).pipe(
      catchError(err => {
        console.error('[HomeService][getResumen] Error:', err);
        return of(null);
      })
    );
  }

  private mapearHomeData(response: any): HomeData {
    const destacados = normalizarProductos(
      Array.isArray(response?.destacados) ? response.destacados : []
    );
    const ofertas = normalizarProductos(
      Array.isArray(response?.ofertas) ? response.ofertas : []
    );
    const nuevos = normalizarProductos(
      Array.isArray(response?.nuevos) ? response.nuevos : []
    );
    const banners = Array.isArray(response?.banners) ? response.banners : [];

    return { destacados, ofertas, nuevos, banners, resumen: response?.resumen };
  }
}
