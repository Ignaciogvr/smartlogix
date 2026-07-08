import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, map, throwError, of } from 'rxjs';

import { BffService } from './bff.service';
import { Producto } from '../../shared/models/producto.model';
import { Comentario } from '../../shared/models/comentario.model';
import { normalizarProducto, normalizarProductos } from '../../shared/utils/producto.util';

@Injectable({ providedIn: 'root' })
export class CatalogoService {

  constructor(
    private http: HttpClient,
    private bff: BffService
  ) {}

  /** Base para endpoints de productos: /api/productos */
  private productosUrl(path = ''): string {
    return `${this.bff.url}/api/productos${path}`;
  }

  private logError(context: string, error: any): void {
    console.error(`[CatalogoService][${context}] Error:`, error);
  }

  // ─── Listados ──────────────────────────────────────────────

  listarProductos(): Observable<Producto[]> {
    return this.http.get<any[]>(this.productosUrl()).pipe(
      map(lista => normalizarProductos(lista)),
      catchError(error => {
        this.logError('listarProductos', error);
        return throwError(() => error);
      })
    );
  }

  listarActivos(): Observable<Producto[]> {
    return this.http.get<any[]>(`${this.bff.url}/api/activos`).pipe(
      map(lista => normalizarProductos(lista)),
      catchError(error => {
        this.logError('listarActivos', error);
        return throwError(() => error);
      })
    );
  }

  obtenerProducto(id: number): Observable<Producto> {
    return this.http.get<any>(this.productosUrl(`/${id}`)).pipe(
      map(raw => normalizarProducto(raw)),
      catchError(error => {
        this.logError(`obtenerProducto(${id})`, error);
        return throwError(() => error);
      })
    );
  }

  // ─── Secciones especiales ──────────────────────────────────

  productosDestacados(): Observable<Producto[]> {
    return this.http.get<any[]>(this.productosUrl('/destacados')).pipe(
      map(lista => normalizarProductos(lista)),
      catchError(error => {
        this.logError('productosDestacados', error);
        return throwError(() => error);
      })
    );
  }

  productosOferta(): Observable<Producto[]> {
    return this.http.get<any[]>(this.productosUrl('/ofertas')).pipe(
      map(lista => normalizarProductos(lista)),
      catchError(error => {
        this.logError('productosOferta', error);
        return throwError(() => error);
      })
    );
  }

  productosNuevos(): Observable<Producto[]> {
    return this.http.get<any[]>(this.productosUrl('/nuevos')).pipe(
      map(lista => normalizarProductos(lista)),
      catchError(error => {
        this.logError('productosNuevos', error);
        return throwError(() => error);
      })
    );
  }

  // ─── Búsqueda y Filtros ────────────────────────────────────

  buscarProductos(termino: string): Observable<Producto[]> {
    const q = termino.trim();
    if (!q) return this.listarProductos();

    return this.http.get<any[]>(this.productosUrl('/buscar'), {
      params: new HttpParams().set('q', q)
    }).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : (lista as any)?.data || [])),
      catchError(error => {
        this.logError('buscarProductos', error);
        return throwError(() => error);
      })
    );
  }

  filtrarProductos(
    precioMin?: number,
    precioMax?: number,
    marca?: string,
    ratingMin?: number
  ): Observable<Producto[]> {
    let params = new HttpParams();
    if (precioMin != null) params = params.set('precioMin', precioMin);
    if (precioMax != null) params = params.set('precioMax', precioMax);
    if (marca) params = params.set('marca', marca);
    if (ratingMin != null) params = params.set('ratingMin', ratingMin);

    return this.http.get<any>(`${this.bff.url}/busqueda/filtrar`, { params }).pipe(
      map(response => normalizarProductos(Array.isArray(response) ? response : response?.data || [])),
      catchError(error => {
        this.logError('filtrarProductos', error);
        // Fallback: listar todos si falla el filtro
        return this.listarProductos();
      })
    );
  }

  listarPorCategoria(categoria: string): Observable<Producto[]> {
    return this.http.get<any[]>(
      this.productosUrl(`/categoria/${encodeURIComponent(categoria)}`)
    ).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : [])),
      catchError(error => {
        this.logError(`listarPorCategoria(${categoria})`, error);
        return throwError(() => error);
      })
    );
  }

  // ─── Banners ──────────────────────────────────────────────

  listarBanners(): Observable<any[]> {
    return this.http.get<any>(`${this.bff.url}/api/home/banners`).pipe(
      map(response => {
        // El BFF puede devolver array directo o { data: [...] }
        if (Array.isArray(response)) return response;
        if (Array.isArray(response?.data)) return response.data;
        if (Array.isArray(response?.banners)) return response.banners;
        return [];
      }),
      catchError(error => {
        this.logError('listarBanners', error);
        // Fallback: banners vacíos (no romper la home)
        return of([]);
      })
    );
  }

  // ─── Comentarios ─────────────────────────────────────────

  obtenerComentarios(id: number): Observable<Comentario[]> {
    return this.http.get<any>(this.productosUrl(`/${id}/comentarios`)).pipe(
      map(response => Array.isArray(response) ? response : response?.data || []),
      catchError(error => {
        this.logError(`obtenerComentarios(${id})`, error);
        return of([]);
      })
    );
  }

  agregarComentario(
    id: number,
    request: { calificacion: number; comentario: string }
  ): Observable<Comentario> {
    return this.http.post<Comentario>(this.productosUrl(`/${id}/comentarios`), request).pipe(
      catchError(error => {
        this.logError(`agregarComentario(${id})`, error);
        return throwError(() => error);
      })
    );
  }

  actualizarComentario(
    comentarioId: number,
    request: { calificacion: number; comentario: string }
  ): Observable<Comentario> {
    return this.http.put<Comentario>(
      `${this.bff.url}/api/comentarios/${comentarioId}`,
      request
    ).pipe(
      catchError(error => {
        this.logError(`actualizarComentario(${comentarioId})`, error);
        return throwError(() => error);
      })
    );
  }

  eliminarComentario(comentarioId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.bff.url}/api/comentarios/${comentarioId}`
    ).pipe(
      catchError(error => {
        this.logError(`eliminarComentario(${comentarioId})`, error);
        return throwError(() => error);
      })
    );
  }

  // ─── Productos Relacionados ──────────────────────────────

  productosRelacionados(id: number): Observable<Producto[]> {
    return this.http.get<any[]>(this.productosUrl(`/${id}/relacionados`)).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : [])),
      catchError(error => {
        this.logError(`productosRelacionados(${id})`, error);
        return of([]);
      })
    );
  }

  productosRelacionadosMarca(id: number): Observable<Producto[]> {
    return this.http.get<any[]>(this.productosUrl(`/${id}/relacionados-marca`)).pipe(
      map(lista => normalizarProductos(Array.isArray(lista) ? lista : [])),
      catchError(error => {
        this.logError(`productosRelacionadosMarca(${id})`, error);
        return of([]);
      })
    );
  }

  // ─── Vistas ──────────────────────────────────────────────

  registrarVista(id: number): Observable<void> {
    return this.http.post<void>(this.productosUrl(`/${id}/vistas`), {}).pipe(
      catchError(() => of(undefined as any))
    );
  }

  // ─── Categorías ──────────────────────────────────────────

  listarCategorias(): Observable<any[]> {
    return this.http.get<any[]>(`${this.bff.url}/api/categorias`).pipe(
      catchError(error => {
        this.logError('listarCategorias', error);
        return of([]);
      })
    );
  }
}
