import {
  Component,
  Input,
  OnInit,
  OnDestroy
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

interface BannerItem {
  id: number;
  titulo: string;
  descripcion: string;
  color: string;
  icono: string;
  ruta: string;
  cta: string;
}

@Component({
  selector: 'app-banner-carousel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './banner-carousel.html',
  styleUrls: ['./banner-carousel.css']
})
export class BannerCarouselComponent
  implements OnInit, OnDestroy {

  @Input()
  banners: any[] = []; // Los banners del BFF si existen

  // Banners funcionales por defecto (sin dependencia de imágenes)
  bannersDefault: BannerItem[] = [
    {
      id: 1,
      titulo: '✨ Ofertas Especiales',
      descripcion: 'Descubre productos con hasta 50% de descuento',
      color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      icono: '🎯',
      ruta: '/catalogo?filter=ofertas',
      cta: 'Ver Ofertas'
    },
    {
      id: 2,
      titulo: '🚀 Nuevos Productos',
      descripcion: 'Lo último en tecnología ya disponible',
      color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
      icono: '⭐',
      ruta: '/catalogo?filter=nuevos',
      cta: 'Explorar'
    },
    {
      id: 3,
      titulo: '💻 Gaming Zone',
      descripcion: 'Equipos para gamers de todos los niveles',
      color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
      icono: '🎮',
      ruta: '/catalogo?categoria=Gaming',
      cta: 'Ver Gaming'
    },
    {
      id: 4,
      titulo: '🏢 Soluciones Empresariales',
      descripcion: 'Tecnología profesional para tu negocio',
      color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
      icono: '💼',
      ruta: '/catalogo?categoria=Empresarial',
      cta: 'Cotizar'
    }
  ];

  currentIndex = 0;
  bannersActivos: BannerItem[] = [];

  private intervalId: any;

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Si no hay banners del backend, usar los por defecto
    if (!this.banners || this.banners.length === 0) {
      this.bannersActivos = this.bannersDefault;
    } else {
      // Convertir banners del backend al formato funcional
      this.bannersActivos = this.banners.map((b, i) => ({
        id: b.id || i,
        titulo: b.titulo || b.title || 'Oferta Especial',
        descripcion: b.descripcion || b.subtitle || 'Descubre nuestros productos',
        color: this.bannersDefault[i % this.bannersDefault.length].color,
        icono: this.bannersDefault[i % this.bannersDefault.length].icono,
        ruta: b.ruta || b.linkUrl || '/catalogo',
        cta: b.cta || 'Ver Más'
      }));
    }

    this.startAutoSlide();
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  nextSlide(): void {
    this.currentIndex =
      (this.currentIndex + 1) %
      this.bannersActivos.length;
  }

  prevSlide(): void {
    this.currentIndex =
      (this.currentIndex - 1 + this.bannersActivos.length) %
      this.bannersActivos.length;
  }

  goToSlide(index: number): void {
    this.currentIndex = index;
  }

  onBannerClick(banner: BannerItem): void {
    if (banner.ruta) {
      this.router.navigate([banner.ruta]);
    }
  }

  private startAutoSlide(): void {
    this.intervalId = setInterval(() => {
      this.nextSlide();
    }, 5000);
  }
}