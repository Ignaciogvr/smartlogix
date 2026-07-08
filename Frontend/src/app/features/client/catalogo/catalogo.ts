import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { CatalogoService } from '../../../core/services/catalogo.service';
import { Producto } from '../../../shared/models/producto.model';
import { ProductCardComponent } from '../../../shared/components/product-card/product-card';
import { SearchBarComponent } from '../../../shared/components/search-bar/search-bar';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ProductCardComponent, SearchBarComponent],
  templateUrl: './catalogo.html',
  styleUrls: ['./catalogo.css']
})
export class CatalogoComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);

  // Datos
  todos: Producto[] = [];
  productos: Producto[] = [];

  // Filtros activos
  categoriaActiva = '';
  searchText = '';
  precioMin: number | null = null;
  precioMax: number | null = null;
  soloConStock = false;
  ordenamiento = 'defecto';

  // Estado UI
  loading = false;
  error = '';

  readonly categorias = ['Periféricos', 'Monitores', 'Muebles', 'Audio', 'Laptops', 'Accesorios'];

  constructor(
    private readonly catalogoService: CatalogoService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.categoriaActiva = params['categoria'] || '';
      this.cargarProductos();
    });
  }

  cargarProductos(): void {
    this.loading = true;
    this.error = '';

    this.catalogoService.listarProductos().subscribe({
      next: (response) => {
        this.todos = response;
        this.aplicarFiltros();
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron cargar los productos. Intenta de nuevo.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  aplicarFiltros(): void {
    this.loading = true;
    
    // Si hay búsqueda por texto, usamos buscarProductos (simplificado por ahora, idealmente se combina en un solo endpoint si se rediseña, pero aquí usamos las opciones según disponibilidad)
    // Para cumplir el requisito, si hay texto usamos buscarProductos del backend, si no, usamos filtrarProductos.
    
    const obs$ = this.searchText.trim()
      ? this.catalogoService.buscarProductos(this.searchText)
      : (this.categoriaActiva || this.precioMin != null || this.precioMax != null || this.soloConStock) 
        ? this.catalogoService.filtrarProductos(this.precioMin || undefined, this.precioMax || undefined, undefined, undefined) // marca y rating no están en UI base
        : this.catalogoService.listarProductos();

    obs$.subscribe({
      next: (response) => {
        let resultado = response;
        
        // Filtro local adicional para categoría si estamos en modo "buscarProductos"
        if (this.searchText.trim() && this.categoriaActiva) {
          resultado = resultado.filter(p => p.categoria.toLowerCase() === this.categoriaActiva.toLowerCase());
        }

        // Filtro por categoría si se llamó listarProductos en lugar de filtrarProductos 
        // (Aunque lo ideal es que filtrarProductos acepte categoría)
        if (!this.searchText.trim() && this.categoriaActiva) {
           resultado = resultado.filter(p => p.categoria.toLowerCase() === this.categoriaActiva.toLowerCase());
        }

        // Filtro solo con stock local (porque el backend no lo pide directamente en la firma de filtrar)
        if (this.soloConStock) {
          resultado = resultado.filter(p => p.stock > 0);
        }

        // Ordenamiento local
        switch (this.ordenamiento) {
          case 'precio_asc':
            resultado.sort((a, b) => a.precio - b.precio);
            break;
          case 'precio_desc':
            resultado.sort((a, b) => b.precio - a.precio);
            break;
          case 'nombre_asc':
            resultado.sort((a, b) => a.nombre.localeCompare(b.nombre));
            break;
          case 'nombre_desc':
            resultado.sort((a, b) => b.nombre.localeCompare(a.nombre));
            break;
        }

        this.productos = resultado;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron aplicar los filtros.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  seleccionarCategoria(cat: string): void {
    this.categoriaActiva = this.categoriaActiva === cat ? '' : cat;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: this.categoriaActiva ? { categoria: this.categoriaActiva } : {},
      queryParamsHandling: 'merge'
    });
    this.aplicarFiltros();
  }

  onBuscar(termino: string): void {
    this.searchText = termino;
    this.aplicarFiltros();
  }

  onPrecioMinChange(val: string): void {
    this.precioMin = val ? Number(val) : null;
    this.aplicarFiltros();
  }

  onPrecioMaxChange(val: string): void {
    this.precioMax = val ? Number(val) : null;
    this.aplicarFiltros();
  }

  onStockChange(): void {
    this.aplicarFiltros();
  }

  onOrdenamientoChange(): void {
    this.aplicarFiltros();
  }

  limpiarFiltros(): void {
    this.categoriaActiva = '';
    this.searchText = '';
    this.precioMin = null;
    this.precioMax = null;
    this.soloConStock = false;
    this.ordenamiento = 'defecto';
    this.router.navigate([], { relativeTo: this.route, queryParams: {} });
    this.aplicarFiltros();
  }

  cantidadPorCategoria(cat: string): number {
    return this.todos.filter(p => p.categoria.toLowerCase() === cat.toLowerCase()).length;
  }

  hayFiltrosActivos(): boolean {
    return !!(this.categoriaActiva || this.searchText || this.precioMin || this.precioMax || this.soloConStock || this.ordenamiento !== 'defecto');
  }

  irADetalle(id: number): void {
    this.router.navigate(['/producto', id]);
  }
}