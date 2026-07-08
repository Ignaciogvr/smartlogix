import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EnvioService } from '../../../core/services/envio.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';

@Component({
  selector: 'app-envios-chofer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './envios.html',
  styleUrls: ['./envios.css']
})
export class EnviosChoferComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private envioService = inject(EnvioService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);

  envios: any[] = [];
  enviosFiltrados: any[] = [];
  loading = false;
  accionId: number | null = null;
  
  filtroEstado = 'TODOS';
  
  estadosDisponibles = [
    { value: 'TODOS', label: 'Todos' },
    { value: 'PENDIENTE', label: 'Pendientes' },
    { value: 'EN_CAMINO', label: 'En Camino' },
    { value: 'ENTREGADO', label: 'Entregados' }
  ];

  ngOnInit(): void {
    this.cargarEnvios();
  }

  cargarEnvios(): void {
    this.loading = true;

    // GET /chofer/envios → envíos asignados al chofer autenticado
    this.envioService.choferEnvios().subscribe({
      next: (envios: any) => {
        this.envios = Array.isArray(envios) ? envios : (envios?.data || []);
        this.aplicarFiltros();
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.toastService.error('Error al cargar tus envíos asignados');
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  aplicarFiltros(): void {
    if (this.filtroEstado === 'TODOS') {
      this.enviosFiltrados = [...this.envios];
    } else {
      this.enviosFiltrados = this.envios.filter(e => e.estado === this.filtroEstado);
    }
    this.cdr.markForCheck();
  }

  async actualizarEstado(envio: any, nuevoEstado: string): Promise<void> {
    const confirmado = await this.confirmService.ask({
      title: 'Actualizar Estado',
      message: `¿Confirmar cambio de estado a ${nuevoEstado}?`,
      confirmText: 'Actualizar'
    });

    if (!confirmado) return;

    this.accionId = envio.id;
    this.cdr.markForCheck();

    this.envioService.actualizarEstado(envio.id, nuevoEstado).subscribe({
      next: () => {
        this.toastService.success('Estado actualizado correctamente');
        this.accionId = null;
        this.cargarEnvios();
      },
      error: (err) => {
        this.toastService.error('Error al actualizar el estado del envío');
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }

  puedeIniciar(envio: any): boolean {
    return envio.estado === 'PENDIENTE';
  }

  puedeEntregar(envio: any): boolean {
    return envio.estado === 'EN_CAMINO';
  }
}
