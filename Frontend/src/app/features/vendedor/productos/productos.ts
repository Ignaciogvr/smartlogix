import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendedorService } from '../../../core/services/vendedor.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { getErrorMessage } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-productos-vendedor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.html',
  styleUrls: ['./productos.css']
})
export class ProductosVendedorComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private vendedorService = inject(VendedorService);
  private toastService = inject(ToastService);

  productos: any[] | null = null;
  loading = true;
  filtro = '';
  productoDialog = false;
  producto: any = {};
  submitted = false;
  guardando = false;
  estados = ['ACTIVO', 'INACTIVO', 'AGOTADO'];

  ngOnInit() {
    this.cargarProductos();
  }

  cargarProductos() {
    this.loading = true;
    this.vendedorService.getProductos().subscribe({
      next: (data) => {
        this.productos = data || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[ProductosVendedor] Error cargando productos:', err);
        const message = getErrorMessage(err, 'al cargar los productos');
        this.toastService.error(message);
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  get productosFiltrados(): any[] {
    if (!this.productos) return [];
    if (!this.filtro.trim()) return this.productos;
    const q = this.filtro.toLowerCase();
    return this.productos.filter(p =>
      p.nombre?.toLowerCase().includes(q) ||
      p.categoria?.toLowerCase().includes(q)
    );
  }

  openNew() {
    this.producto = {};
    this.submitted = false;
    this.productoDialog = true;
  }

  editProducto(prod: any) {
    this.producto = { ...prod };
    this.productoDialog = true;
  }

  hideDialog() {
    this.productoDialog = false;
    this.submitted = false;
  }

  saveProducto() {
    this.submitted = true;
    if (!this.producto.nombre?.trim() || !this.producto.precio || this.producto.stock == null) return;
    this.guardando = true;
    if (this.producto.id) {
      this.vendedorService.actualizarProducto(this.producto.id, this.producto).subscribe({
        next: () => {
          this.toastService.success('Producto actualizado correctamente');
          this.cargarProductos();
          this.productoDialog = false;
          this.guardando = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('[ProductosVendedor] Error actualizando producto:', err);
          const message = getErrorMessage(err, 'al actualizar el producto');
          this.toastService.error(message);
          this.guardando = false;
          this.cdr.markForCheck();
        }
      });
    } else {
      this.vendedorService.crearProducto(this.producto).subscribe({
        next: () => {
          this.toastService.success('Producto creado correctamente');
          this.cargarProductos();
          this.productoDialog = false;
          this.guardando = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('[ProductosVendedor] Error creando producto:', err);
          const message = getErrorMessage(err, 'al crear el producto');
          this.toastService.error(message);
          this.guardando = false;
          this.cdr.markForCheck();
        }
      });
    }
  }

  getSeverityClass(estado: string): string {
    switch (estado) {
      case 'ACTIVO': return 'badge-success';
      case 'AGOTADO': return 'badge-warning';
      case 'INACTIVO': return 'badge-danger';
      default: return 'badge-info';
    }
  }
}
