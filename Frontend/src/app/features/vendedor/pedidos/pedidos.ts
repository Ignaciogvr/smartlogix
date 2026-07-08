import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendedorService } from '../../../core/services/vendedor.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { HttpErrorResponse } from '@angular/common/http';
import { getErrorType, getErrorMessage, ApiErrorType } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-pedidos-vendedor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pedidos.html',
  styleUrls: ['./pedidos.css']
})
export class PedidosVendedorComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private vendedorService = inject(VendedorService);
  private toastService = inject(ToastService);

  pedidos: any[] | null = null;
  loading = true;
  filtro = '';
  errorType: ApiErrorType | null = null;
  errorMessage = '';

  ngOnInit() {
    this.cargarPedidos();
  }

  cargarPedidos() {
    this.loading = true;
    this.errorType = null;
    this.errorMessage = '';
    
    this.vendedorService.getPedidos().subscribe({
      next: (data) => {
        this.pedidos = data || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error cargando pedidos vendedor:', err);
        this.errorType = getErrorType(err);
        this.errorMessage = getErrorMessage(err);
        this.pedidos = null;
        this.loading = false;
        
        if (this.errorType === ApiErrorType.UNAUTHORIZED) {
          this.toastService.error('Sesión expirada. Por favor inicia sesión nuevamente.');
        } else if (this.errorType === ApiErrorType.FORBIDDEN) {
          this.toastService.error('No tienes permisos para ver los pedidos.');
        } else if (this.errorType === ApiErrorType.SERVER_ERROR) {
          this.toastService.error('Error del servidor. Por favor intenta más tarde.');
        } else {
          this.toastService.error('No se pudieron cargar los pedidos');
        }
        
        this.cdr.markForCheck();
      }
    });
  }

  get pedidosFiltrados(): any[] {
    if (!this.pedidos) return [];
    if (!this.filtro.trim()) return this.pedidos;
    const q = this.filtro.toLowerCase();
    return this.pedidos.filter(p =>
      String(p.id).includes(q) ||
      p.cliente?.toLowerCase().includes(q) ||
      p.estado?.toLowerCase().includes(q)
    );
  }

  getBadgeClass(estado: string): string {
    switch (estado) {
      case 'ENTREGADO': return 'badge-success';
      case 'ENVIADO': return 'badge-info';
      case 'PENDIENTE': return 'badge-warning';
      case 'CANCELADO': return 'badge-danger';
      default: return 'badge-secondary';
    }
  }
}
