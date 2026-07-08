import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { forkJoin } from 'rxjs';

import { HomeService } from '../../../core/services/home.service';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { Producto } from '../../../shared/models/producto.model';
import { BannerCarouselComponent } from '../../../shared/components/banner-carousel/banner-carousel';
import { ProductCardComponent } from '../../../shared/components/product-card/product-card';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner';

interface CategoriaInfo {
  nombre: string;
  icono: string;
  cantidad: number;
  color: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, BannerCarouselComponent, ProductCardComponent, LoadingSpinnerComponent],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);

  productosDestacados: Producto[] = [];
  productosOferta: Producto[] = [];
  productosNuevos: Producto[] = [];
  masVendidos: Producto[] = [];
  categorias: CategoriaInfo[] = [];

  loading = false;
  error = '';

  anuncios: any[] = [];

  resumen: any = null;

  constructor(
    private readonly homeService: HomeService,
    private readonly catalogoService: CatalogoService
  ) {}

  ngOnInit(): void {
    this.cargarHome();
  }

  cargarHome(): void {
    this.loading = true;
    this.error = '';

    // Usar forkJoin para obtener todos los datos en paralelo
    forkJoin({
      homeData: this.homeService.getDatosHome(),
      banners: this.homeService.getBanners(),
      todos: this.catalogoService.listarProductos()
    }).subscribe({
      next: ({ homeData, banners, todos }) => {
        // Destacados desde el endpoint dedicado
        this.productosDestacados = homeData.destacados.length > 0
          ? homeData.destacados.slice(0, 6)
          : todos.filter(p => p.destacado).slice(0, 6);

        // Ofertas desde el endpoint dedicado
        this.productosOferta = homeData.ofertas.length > 0
          ? homeData.ofertas.slice(0, 6)
          : todos.filter(p => p.oferta).slice(0, 6);

        // Nuevos desde el endpoint dedicado
        this.productosNuevos = homeData.nuevos.length > 0
          ? homeData.nuevos.slice(0, 6)
          : todos.filter(p => p.nuevo).slice(0, 6);

        // Más vendidos: ordenar por vendidos desc
        this.masVendidos = [...todos]
          .sort((a, b) => (b.vendidos ?? 0) - (a.vendidos ?? 0))
          .slice(0, 4);

        // Calcular categorías con conteo real
        this.calcularCategorias(todos);

        // Banners reales del BFF
        this.anuncios = banners.map((b: any) => ({
          id: b.id,
          imagen: b.imagenUrl || b.imageUrl || b.imagen || '',
          titulo: b.titulo || b.title || '',
          descripcion: b.descripcion || b.subtitle || '',
          ruta: b.rutaDestino || b.linkUrl || '/catalogo'
        }));

        this.resumen = homeData.resumen;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[HomeComponent] Error cargando home:', err);
        this.error = 'Error al cargar la página principal. Por favor recarga.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  private calcularCategorias(todos: Producto[]): void {
    const mapa: Record<string, { icono: string; color: string }> = {
      'Periféricos': { icono: '🖱️', color: '#6366f1' },
      'Monitores': { icono: '🖥️', color: '#0ea5e9' },
      'Muebles': { icono: '🪑', color: '#f59e0b' },
      'Audio': { icono: '🎧', color: '#10b981' },
      'Laptops': { icono: '💻', color: '#8b5cf6' },
      'Accesorios': { icono: '🔌', color: '#f43f5e' },
      'Redes': { icono: '🌐', color: '#06b6d4' },
      'Gaming': { icono: '🎮', color: '#ec4899' },
      'Almacenamiento': { icono: '💾', color: '#84cc16' },
      'Smart Home': { icono: '🏠', color: '#f97316' }
    };

    // Contar por categoría
    const conteo: Record<string, number> = {};
    for (const p of todos) {
      if (p.categoria) {
        conteo[p.categoria] = (conteo[p.categoria] || 0) + 1;
      }
    }

    // Categorías con productos reales (del BFF)
    const categoriasReales = Object.entries(conteo).map(([nombre, cantidad]) => ({
      nombre,
      icono: mapa[nombre]?.icono || '📦',
      color: mapa[nombre]?.color || '#6366f1',
      cantidad
    }));

    // Si no hay suficientes del BFF, complementar con las del mapa
    if (categoriasReales.length < 5) {
      this.categorias = Object.entries(mapa).map(([nombre, meta]) => ({
        nombre,
        icono: meta.icono,
        color: meta.color,
        cantidad: conteo[nombre] || 0
      }));
    } else {
      this.categorias = categoriasReales.sort((a, b) => b.cantidad - a.cantidad);
    }
  }
}