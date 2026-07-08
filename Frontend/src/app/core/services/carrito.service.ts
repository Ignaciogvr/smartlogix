import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, of } from 'rxjs';

import { AuthService } from './auth.service';
import { BffService } from './bff.service';
import { normalizarProducto } from '../../shared/utils/producto.util';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private carrito: any[] = [];
  private readonly STORAGE_KEY = 'smartlogix_cart';

  private carritoSubject = new BehaviorSubject<any[]>([]);
  carrito$ = this.carritoSubject.asObservable();

  constructor(
    private auth: AuthService,
    private http: HttpClient,
    private bff: BffService
  ) {
    this.cargarCarritoLocal();
    
    // Si el usuario se loguea, deberíamos cargar el del backend y mezclar. 
    // Por simplicidad, si está autenticado, recargamos.
    if (this.auth.isAuthenticated()) {
      this.sincronizarConBackend();
    }
  }

  private cargarCarritoLocal(): void {
    try {
      const saved = localStorage.getItem(this.STORAGE_KEY);
      if (saved) {
        this.carrito = JSON.parse(saved);
        this.carritoSubject.next([...this.carrito]);
      }
    } catch (e) {
      this.carrito = [];
    }
  }

  private actualizarCarritoLocal(): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(this.carrito));
    this.carritoSubject.next([...this.carrito]);
  }

  private get carritoUrl(): string {
    return `${this.bff.url}/api/carrito`;
  }

  // --- HÍBRIDO ---

  agregarProducto(producto: any, cantidad: number = 1): void {
    const item = normalizarProducto(producto);

    console.log('[CarritoService] Agregando producto:', item.nombre, 'cantidad:', cantidad);

    if (this.auth.isAuthenticated()) {
      // Usuario autenticado: agregar al backend
      this.http.post<any>(`${this.carritoUrl}/items`, {
        productoId: item.id,
        cantidad: cantidad,
        precio: item.precio
      }).subscribe({
        next: (res) => {
          console.log('[CarritoService] Producto agregado al backend:', res);
          if (res?.data?.items) {
            this.procesarRespuestaBff(res);
          } else {
            this.recargarDesdeBackend();
          }
        },
        error: (err) => {
          console.error('[CarritoService] Error agregando al backend:', err);
          // Fallback: agregar localmente si falla el backend
          this.agregarLocalmenteFallback(item, cantidad);
        }
      });
    } else {
      // Usuario NO autenticado: agregar localmente
      this.agregarLocalmenteFallback(item, cantidad);
    }
  }

  private agregarLocalmenteFallback(item: any, cantidad: number): void {
    console.log('[CarritoService] Agregando localmente (fallback):', item.nombre);
    const existente = this.carrito.find((p) => p.id === item.id);
    if (existente) {
      existente.cantidad += cantidad;
    } else {
      this.carrito.push({ ...item, cantidad });
    }
    this.actualizarCarritoLocal();
  }

  agregar(producto: any): void {
    this.agregarProducto(producto, 1);
  }

  obtenerCarrito(): any[] {
    // Para simplificar síncronamente, retornamos el array actual que siempre debería estar al día
    return this.carrito;
  }

  getItems(): any[] {
    return this.carrito;
  }

  eliminarProducto(productoId: number): void {
    if (this.auth.isAuthenticated()) {
      this.http.delete<any>(`${this.carritoUrl}/items/${productoId}`).subscribe({
        next: (res) => {
          if (res?.data?.items) this.procesarRespuestaBff(res);
          else this.recargarDesdeBackend();
        }
      });
    } else {
      this.carrito = this.carrito.filter((item) => item.id !== productoId);
      this.actualizarCarritoLocal();
    }
  }

  aumentarCantidad(productoId: number): void {
    const producto = this.carrito.find((item) => item.id === productoId);
    if (producto) {
      const nuevaCantidad = producto.cantidad + 1;
      if (this.auth.isAuthenticated()) {
        this.http.put<any>(`${this.carritoUrl}/items/${productoId}?cantidad=${nuevaCantidad}`, {}).subscribe({
          next: (res) => {
            if (res?.data?.items) this.procesarRespuestaBff(res);
            else this.recargarDesdeBackend();
          }
        });
      } else {
        producto.cantidad = nuevaCantidad;
        this.actualizarCarritoLocal();
      }
    }
  }

  disminuirCantidad(productoId: number): void {
    const producto = this.carrito.find((item) => item.id === productoId);
    if (producto && producto.cantidad > 1) {
      const nuevaCantidad = producto.cantidad - 1;
      if (this.auth.isAuthenticated()) {
        this.http.put<any>(`${this.carritoUrl}/items/${productoId}?cantidad=${nuevaCantidad}`, {}).subscribe({
          next: (res) => {
            if (res?.data?.items) this.procesarRespuestaBff(res);
            else this.recargarDesdeBackend();
          }
        });
      } else {
        producto.cantidad = nuevaCantidad;
        this.actualizarCarritoLocal();
      }
    }
  }

  vaciarCarrito(): void {
    if (this.auth.isAuthenticated()) {
      this.http.delete<any>(this.carritoUrl).subscribe({
        next: (res) => {
          this.carrito = [];
          this.carritoSubject.next([...this.carrito]);
        }
      });
    } else {
      this.carrito = [];
      this.actualizarCarritoLocal();
    }
  }

  limpiarCarrito(): void {
    this.vaciarCarrito();
  }

  limpiar(): void {
    this.vaciarCarrito();
  }

  obtenerSubtotal(): number {
    return this.carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
  }

  obtenerTotal(): number {
    return this.obtenerSubtotal();
  }

  // --- MÉTODOS DE SINCRONIZACIÓN ---

  private recargarDesdeBackend(): void {
    if (!this.auth.isAuthenticated()) return;
    
    this.http.get<any>(this.carritoUrl).subscribe({
      next: (res) => this.procesarRespuestaBff(res)
    });
  }

  private procesarRespuestaBff(res: any): void {
    if (res?.data?.items) {
      this.carrito = res.data.items.map((i: any) => ({
        id: i.productoId,
        cantidad: i.cantidad,
        precio: i.precioUnitario || i.precio,
        nombre: i.nombre || '', 
        imagenUrl: i.imagenUrl || ''
      }));
      this.carritoSubject.next([...this.carrito]);
    } else if (res?.data && !res.data.items) {
      // Si el carrito está vacío
      this.carrito = [];
      this.carritoSubject.next([...this.carrito]);
    }
  }

  private sincronizarConBackend(): void {
    // 1. Obtener carrito local
    const local = [...this.carrito];
    
    // 2. Limpiar local
    this.carrito = [];
    localStorage.removeItem(this.STORAGE_KEY);
    
    // 3. Obtener de backend y subir lo local
    if (local.length > 0) {
      // Subir items locales uno por uno (simplificado)
      local.forEach(item => {
        this.http.post<any>(`${this.carritoUrl}/items`, {
          productoId: item.id,
          cantidad: item.cantidad,
          precio: item.precio
        }).subscribe();
      });
      setTimeout(() => this.recargarDesdeBackend(), 1000);
    } else {
      this.recargarDesdeBackend();
    }
  }
}
