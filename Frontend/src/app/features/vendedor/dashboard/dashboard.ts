import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { VendedorService } from '../../../core/services/vendedor.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { getErrorMessage, getErrorType, ApiErrorType } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-dashboard-vendedor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardVendedorComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private vendedorService = inject(VendedorService);
  private toastService = inject(ToastService);

  metrics: any = null;
  loading = true;
  error = false;
  errorMessage = '';
  errorType: ApiErrorType | null = null;

  ngOnInit() {
    this.cargarDashboard();
  }

  cargarDashboard() {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    this.errorType = null;

    this.vendedorService.getDashboardMetrics().subscribe({
      next: (data) => {
        this.metrics = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[DashboardVendedor] Error cargando métricas:', err);
        
        this.errorType = getErrorType(err);
        this.errorMessage = getErrorMessage(err, 'al cargar las métricas');
        this.error = true;
        this.loading = false;
        
        if (this.errorType === ApiErrorType.SERVER_ERROR || 
            this.errorType === ApiErrorType.NETWORK) {
          this.toastService.error(this.errorMessage);
        }
        
        this.cdr.markForCheck();
      }
    });
  }

  get isServerError(): boolean {
    return this.errorType === ApiErrorType.SERVER_ERROR;
  }

  get isNetworkError(): boolean {
    return this.errorType === ApiErrorType.NETWORK;
  }

  get isNotFoundError(): boolean {
    return this.errorType === ApiErrorType.NOT_FOUND;
  }

  getSeverity(estado: string): string {
    switch (estado) {
      case 'ENTREGADO': return 'badge-success';
      case 'ENVIADO': return 'badge-info';
      case 'PENDIENTE': return 'badge-warning';
      case 'CANCELADO': return 'badge-danger';
      default: return '';
    }
  }
}
