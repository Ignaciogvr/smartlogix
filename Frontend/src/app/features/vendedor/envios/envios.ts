import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendedorService } from '../../../core/services/vendedor.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { getErrorMessage } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-envios-vendedor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './envios.html',
  styleUrls: ['./envios.css']
})
export class EnviosVendedorComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private vendedorService = inject(VendedorService);
  private toastService = inject(ToastService);

  envios: any[] | null = null;
  loading = true;
  actualizandoId: number | null = null;

  estados = [
    { label: 'Preparando', value: 'PREPARANDO' },
    { label: 'Enviado', value: 'ENVIADO' },
    { label: 'Entregado', value: 'ENTREGADO' }
  ];

  ngOnInit() {
    this.cargarEnvios();
  }

  cargarEnvios() {
    this.loading = true;
    this.vendedorService.getEnvios().subscribe({
      next: (data) => {
        this.envios = data || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[EnviosVendedor] Error cargando envíos:', err);
        const message = getErrorMessage(err, 'al cargar los envíos');
        this.toastService.error(message);
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  cambiarEstado(envioId: number, nuevoEstado: string) {
    this.actualizandoId = envioId;
    this.cdr.markForCheck();
    this.vendedorService.actualizarEstadoEnvio(envioId, nuevoEstado).subscribe({
      next: () => {
        this.toastService.success('Estado actualizado correctamente');
        this.actualizandoId = null;
        this.cargarEnvios();
      },
      error: (err) => {
        console.error('[EnviosVendedor] Error actualizando estado:', err);
        const message = getErrorMessage(err, 'al actualizar el estado');
        this.toastService.error(message);
        this.actualizandoId = null;
        this.cdr.markForCheck();
      }
    });
  }

  getBadgeClass(estado: string): string {
    switch (estado) {
      case 'ENTREGADO': return 'badge-success';
      case 'ENVIADO': return 'badge-info';
      case 'PREPARANDO': return 'badge-warning';
      default: return 'badge-secondary';
    }
  }
}
