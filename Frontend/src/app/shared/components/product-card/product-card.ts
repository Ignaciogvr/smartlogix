import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CarritoService } from '../../../core/services/carrito.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../toast/toast.service';
import {
  imagenAlternativa,
  imagenProducto
} from '../../utils/producto.util';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-card.html',
  styleUrls: ['./product-card.css']
})
export class ProductCardComponent {

  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  @Input() producto: any;

  @Output() agregar = new EventEmitter<any>();

  agregarAlCarrito(event?: Event): void {
    if (event) {
      event.stopPropagation();
    }

    if (!this.producto) {
      console.warn('[ProductCard] Intento de agregar producto nulo al carrito');
      return;
    }

    if (!this.tieneStock()) {
      this.toastService.show('Producto sin stock disponible', 'warning');
      return;
    }

    console.log('[ProductCard] Agregando al carrito:', this.producto.nombre);
    this.carritoService.agregarProducto(this.producto, 1);
    this.toastService.success(`${this.producto.nombre} agregado al carrito`);
    this.agregar.emit(this.producto);
  }

  verDetalle(): void {
    this.router.navigate(['/producto', this.producto.id]);
  }

  tieneStock(): boolean {
    return this.producto?.stock > 0;
  }

  stockLabel(): string {
    const stock = this.producto?.stock ?? 0;
    if (stock === 0) return 'Agotado';
    if (stock <= 5) return `¡Solo ${stock} disponibles!`;
    return 'En stock';
  }

  imagenUrl(): string {
    const url = imagenProducto(this.producto);
    if (!url || url.trim() === '') return imagenAlternativa();
    return url;
  }

  onImagenError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = imagenAlternativa();
    img.onerror = null;
  }

  esOferta(): boolean {
    return !!(this.producto?.oferta && this.producto?.precioAnterior);
  }

  descuento(): number {
    return this.producto?.descuentoPorcentaje ?? 0;
  }

  precioFinal(): number {
    return this.producto?.precio ?? 0;
  }

  precioOriginal(): number {
    return this.producto?.precioAnterior ?? this.producto?.precio ?? 0;
  }

  ratingStars(): string {
    const r = Math.round(this.producto?.rating ?? 0);
    return '★'.repeat(r) + '☆'.repeat(5 - r);
  }

  ratingPromedio(): number {
    return this.producto?.rating ?? 0;
  }

  totalOpiniones(): number {
    return this.producto?.totalRatings ?? 0;
  }

  tieneMarca(): boolean {
    return !!(this.producto?.marca);
  }
}