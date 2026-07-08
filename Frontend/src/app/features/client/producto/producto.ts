import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { CatalogoService } from '../../../core/services/catalogo.service';
import { CarritoService } from '../../../core/services/carrito.service';
import { AuthService } from '../../../core/services/auth.service';

import { Producto } from '../../../shared/models/producto.model';
import { Comentario } from '../../../shared/models/comentario.model';
import { imagenAlternativa, imagenProducto } from '../../../shared/utils/producto.util';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner';
import { ProductCardComponent } from '../../../shared/components/product-card/product-card';

@Component({
  selector: 'app-producto',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent, ProductCardComponent],
  templateUrl: './producto.html',
  styleUrls: ['./producto.css']
})
export class ProductoComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly authService = inject(AuthService);

  producto!: Producto;
  comentarios: Comentario[] = [];
  relacionados: Producto[] = [];

  productoId!: number;
  cantidad = 1;
  imagenSeleccionada = '';
  tabActiva: 'descripcion' | 'especificaciones' | 'comentarios' = 'descripcion';
  
  loading = false;
  loadingComentarios = false;
  loadingRelacionados = false;
  error = '';

  // Toast notification
  toastVisible = false;
  toastMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly catalogoService: CatalogoService,
    private readonly carritoService: CarritoService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productoId = Number(params.get('id'));
      this.tabActiva = 'descripcion';
      this.comentarios = [];
      this.relacionados = [];
      this.obtenerProducto();
    });
  }

  obtenerProducto(): void {
    this.loading = true;
    this.error = '';

    this.catalogoService.obtenerProducto(this.productoId).subscribe({
      next: (response) => {
        this.producto = response;
        this.imagenSeleccionada = imagenProducto(response);
        this.loading = false;
        this.cdr.markForCheck();
        this.cargarComentarios();
        this.cargarRelacionados();
        // Registrar vista en el BFF (POST /api/productos/{id}/vistas)
        this.catalogoService.registrarVista(this.productoId).subscribe();
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo cargar el producto.';
        this.cdr.markForCheck();
      }
    });
  }

  cargarComentarios(): void {
    this.loadingComentarios = true;
    this.catalogoService.obtenerComentarios(this.productoId).subscribe({
      next: (c) => {
        this.comentarios = c;
        this.loadingComentarios = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingComentarios = false;
        this.cdr.markForCheck();
      }
    });
  }

  cargarRelacionados(): void {
    this.loadingRelacionados = true;
    this.catalogoService.productosRelacionados(this.productoId).subscribe({
      next: (r) => {
        this.relacionados = r.slice(0, 4);
        this.loadingRelacionados = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingRelacionados = false;
        this.cdr.markForCheck();
      }
    });
  }

  seleccionarImagen(url: string): void {
    this.imagenSeleccionada = url;
  }

  onImagenError(event: Event): void {
    const img = event.target as HTMLImageElement;
    this.imagenSeleccionada = imagenAlternativa();
    img.src = this.imagenSeleccionada;
    img.onerror = null;
  }

  imagenesProducto(): string[] {
    if (!this.producto) return [];
    return this.producto.imagenes?.length
      ? this.producto.imagenes
      : (this.producto.imagenUrl ? [this.producto.imagenUrl] : []);
  }

  aumentarCantidad(): void {
    if (this.cantidad < (this.producto?.stock ?? 0)) this.cantidad++;
  }

  puedeAumentarCantidad(): boolean {
    return this.cantidad < (this.producto?.stock ?? 0);
  }

  disminuirCantidad(): void {
    if (this.cantidad > 1) this.cantidad--;
  }

  agregarAlCarrito(): void {
    if (this.producto) {
      this.carritoService.agregarProducto(this.producto, this.cantidad);
      this.showToast('✅ Producto agregado al carrito');
    }
  }

  comprarAhora(): void {
    if (!this.producto) return;
    if (!this.authService.isAuthenticated()) {
      this.authService.login().subscribe();
      return;
    }
    this.carritoService.agregarProducto(this.producto, this.cantidad);
    this.router.navigate(['/checkout']);
  }

  showToast(msg: string): void {
    this.toastMessage = msg;
    this.toastVisible = true;
    setTimeout(() => { this.toastVisible = false; }, 3000);
  }

  // --- Helpers para el template ---

  esOferta(): boolean {
    return !!(this.producto?.oferta && this.producto?.precioAnterior);
  }

  precioFinal(): number {
    return this.producto?.precio ?? 0;
  }

  precioOriginal(): number {
    return this.producto?.precioAnterior ?? this.producto?.precio ?? 0;
  }

  descuento(): number {
    return this.producto?.descuentoPorcentaje ?? 0;
  }

  stockLabel(): string {
    const stock = this.producto?.stock ?? 0;
    if (stock === 0) return 'Agotado';
    if (stock <= 5) return `¡Últimas ${stock} unidades!`;
    if (stock <= 20) return 'Stock limitado';
    return 'En stock';
  }

  stockClass(): string {
    const stock = this.producto?.stock ?? 0;
    if (stock === 0) return 'out';
    if (stock <= 5) return 'low';
    return 'ok';
  }

  ratingStars(rating: number): string {
    const r = Math.round(rating);
    return '★'.repeat(r) + '☆'.repeat(5 - r);
  }

  ratingHalfStars(rating: number): string {
    const full = Math.floor(rating);
    const half = rating - full >= 0.5 ? 1 : 0;
    const empty = 5 - full - half;
    return '★'.repeat(full) + (half ? '½' : '') + '☆'.repeat(empty);
  }

  promedioComentarios(): number {
    if (!this.comentarios.length) return this.producto?.rating ?? 0;
    const sum = this.comentarios.reduce((acc, c) => acc + c.calificacion, 0);
    return Math.round((sum / this.comentarios.length) * 10) / 10;
  }

  tiempoRelativo(fecha: string): string {
    const diff = Date.now() - new Date(fecha).getTime();
    const days = Math.floor(diff / 86400000);
    if (days < 1) return 'Hoy';
    if (days < 7) return `Hace ${days} día${days > 1 ? 's' : ''}`;
    const weeks = Math.floor(days / 7);
    if (weeks < 4) return `Hace ${weeks} semana${weeks > 1 ? 's' : ''}`;
    const months = Math.floor(days / 30);
    if (months < 12) return `Hace ${months} mes${months > 1 ? 'es' : ''}`;
    return `Hace ${Math.floor(months / 12)} año${Math.floor(months / 12) > 1 ? 's' : ''}`;
  }

  especificaciones(): { label: string; value: string }[] {
    if (!this.producto) return [];
    const specs: { label: string; value: string }[] = [];
    if (this.producto.marca) specs.push({ label: 'Marca', value: this.producto.marca });
    if (this.producto.modelo) specs.push({ label: 'Modelo', value: this.producto.modelo });
    if (this.producto.fabricante) specs.push({ label: 'Fabricante', value: this.producto.fabricante });
    if (this.producto.sku) specs.push({ label: 'SKU', value: this.producto.sku });
    if (this.producto.peso) specs.push({ label: 'Peso', value: this.producto.peso });
    if (this.producto.dimensiones) specs.push({ label: 'Dimensiones', value: this.producto.dimensiones });
    if (this.producto.material) specs.push({ label: 'Material', value: this.producto.material });
    if (this.producto.color) specs.push({ label: 'Color', value: this.producto.color });
    if (this.producto.paisFabricacion) specs.push({ label: 'País de fabricación', value: this.producto.paisFabricacion });
    return specs;
  }

  especificacionesExisten(): boolean {
    return this.especificaciones().length > 0;
  }
}
