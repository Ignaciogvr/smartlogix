import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CatalogoService } from '../../../core/services/catalogo.service';
import { AdminService } from '../../../core/services/admin.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';
import { getErrorMessage } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-productos-admin',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './productos.html',
  styleUrls: ['./productos.css']
})
export class ProductosComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private catalogoService = inject(CatalogoService);
  private adminService = inject(AdminService);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);

  productos: any[] | null = null;
  productosFiltrados: any[] = [];
  categorias: string[] = [];

  loading = false;
  filtro = '';
  filtroCategoria = '';
  filtroEstado = '';
  filtroOrden = '';

  mostrarModal = false;
  modoEdicion = false;
  productoActual: any = this.getProductoVacio();
  guardando = false;
  eliminandoId: number | null = null;

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.loading = true;

    this.catalogoService
      .listarProductos()
      .subscribe({
        next: (response: any) => {
          this.productos = response || [];
          this.extraerCategorias();
          this.aplicarFiltros();
          this.loading = false;
          this.cdr.markForCheck();
        },
        error: (err: any) => {
          console.error('[ProductosAdmin] Error cargando productos:', err);
          const message = getErrorMessage(err, 'al cargar productos');
          this.toastService.error(message);
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
  }

  extraerCategorias(): void {
    if (!this.productos) return;
    const cats = new Set<string>();
    this.productos.forEach(p => {
      if (p.categoria) cats.add(p.categoria);
    });
    this.categorias = Array.from(cats).sort();
  }

  aplicarFiltros(): void {
    if (!this.productos) return;

    let resultado = [...this.productos];

    // Filtro por nombre
    if (this.filtro.trim()) {
      const termino = this.filtro.toLowerCase();
      resultado = resultado.filter(p =>
        p.nombre?.toLowerCase().includes(termino) ||
        p.descripcion?.toLowerCase().includes(termino)
      );
    }

    // Filtro por categoría
    if (this.filtroCategoria) {
      resultado = resultado.filter(p => p.categoria === this.filtroCategoria);
    }

    // Filtro por estado
    if (this.filtroEstado) {
      resultado = resultado.filter(p => p.estado === this.filtroEstado);
    }

    // Ordenamiento
    switch (this.filtroOrden) {
      case 'stock_bajo':
        resultado = resultado.filter(p => p.stock < 10).sort((a, b) => a.stock - b.stock);
        break;
      case 'mas_vendidos':
        resultado = resultado.sort((a, b) => (b.totalVendidos || 0) - (a.totalVendidos || 0));
        break;
      case 'mejor_calificados':
        resultado = resultado.sort((a, b) => (b.calificacionPromedio || 0) - (a.calificacionPromedio || 0));
        break;
      case 'peor_calificados':
        resultado = resultado.sort((a, b) => (a.calificacionPromedio || 0) - (b.calificacionPromedio || 0));
        break;
      case 'mas_caros':
        resultado = resultado.sort((a, b) => b.precio - a.precio);
        break;
      case 'mas_baratos':
        resultado = resultado.sort((a, b) => a.precio - b.precio);
        break;
    }

    this.productosFiltrados = resultado;
    this.cdr.markForCheck();
  }

  limpiarFiltros(): void {
    this.filtro = '';
    this.filtroCategoria = '';
    this.filtroEstado = '';
    this.filtroOrden = '';
    this.aplicarFiltros();
  }

  abrirModalCrear(): void {
    this.modoEdicion = false;
    this.productoActual = this.getProductoVacio();
    this.mostrarModal = true;
  }

  abrirModalEditar(producto: any): void {
    this.modoEdicion = true;
    this.productoActual = { ...producto };
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.productoActual = this.getProductoVacio();
  }

  guardarProducto(): void {
    this.guardando = true;

    if (this.modoEdicion) {
      this.adminService.actualizarProducto(
        this.productoActual.id,
        this.productoActual
      ).subscribe({
        next: () => {
          this.toastService.success('Producto actualizado correctamente');
          this.guardando = false;
          this.cerrarModal();
          this.cargarProductos();
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('[ProductosAdmin] Error actualizando producto:', err);
          const message = getErrorMessage(err, 'al actualizar el producto');
          this.toastService.error(message);
          this.guardando = false;
          this.cdr.markForCheck();
        }
      });
    } else {
      this.adminService.crearProducto(this.productoActual).subscribe({
        next: () => {
          this.toastService.success('Producto creado correctamente');
          this.guardando = false;
          this.cerrarModal();
          this.cargarProductos();
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('[ProductosAdmin] Error creando producto:', err);
          const message = getErrorMessage(err, 'al crear el producto');
          this.toastService.error(message);
          this.guardando = false;
          this.cdr.markForCheck();
        }
      });
    }
  }

  getProductoVacio(): any {
    return {
      nombre: '',
      descripcion: '',
      precio: 0,
      stock: 0,
      categoria: '',
      marca: '',
      estado: 'ACTIVO',
      imagenPrincipal: ''
    };
  }

  async eliminarProducto(producto: any): Promise<void> {
    const confirmado = await this.confirmService.ask({
      title: 'Eliminar Producto',
      message: `¿Estás seguro de eliminar "${producto.nombre}"?`,
      confirmText: 'Eliminar',
      isDanger: true
    });

    if (!confirmado) {
      return;
    }

    this.eliminandoId = producto.id;
    this.cdr.markForCheck();

    this.adminService.eliminarProducto(producto.id).subscribe({
      next: () => {
        this.toastService.success('Producto eliminado correctamente');
        this.eliminandoId = null;
        this.cargarProductos();
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[ProductosAdmin] Error eliminando producto:', err);
        const message = getErrorMessage(err, 'al eliminar el producto');
        this.toastService.error(message);
        this.eliminandoId = null;
        this.cdr.markForCheck();
      }
    });
  }
}