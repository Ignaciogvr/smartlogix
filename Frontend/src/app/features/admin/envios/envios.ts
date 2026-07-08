import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { EnvioService } from '../../../core/services/envio.service';
import { TrackingService } from '../../../core/services/tracking.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';
import { getErrorMessage, getErrorType } from '../../../shared/models/api-error.model';


@Component({
  selector: 'app-envios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './envios.html',
  styleUrls: ['./envios.css']
})
export class EnviosComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);
  private envioService = inject(EnvioService);
  private trackingService = inject(TrackingService);
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  envios: any[] | null = null;
  choferes: any[] | null = null;
  loading = false;
  guardando = false;
  accionId: number | null = null;

  mostrarModalAsignar = false;
  envioSeleccionado: any = null;
  choferSeleccionado = '';

  ngOnInit(): void {
    this.cargarEnvios();
    this.cargarChoferes();
  }

  cargarEnvios() {
    this.loading = true;

    this.envioService.listar().subscribe({
      next: (data: any) => {
        this.envios = data || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[EnviosAdmin] Error cargando envíos:', err);
        const message = getErrorMessage(err, 'al cargar envíos');
        this.toastService.error(message);
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  cargarChoferes() {
    this.usuarioService.listar().subscribe({
      next: (data: any) => {
        const usuarios = data || [];
        this.choferes = usuarios.filter((u: any) => 
          u.rol === 'CHOFER' || 
          u.roles?.includes('CHOFER') ||
          u.permissions?.includes('CHOFER')
        );
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[EnviosAdmin] Error cargando choferes:', err);
        const message = getErrorMessage(err, 'al cargar choferes');
        this.toastService.error(message);
      }
    });
  }

  abrirModalAsignar(envio: any) {
    this.envioSeleccionado = envio;
    this.choferSeleccionado = '';
    this.mostrarModalAsignar = true;
  }

  cerrarModalAsignar() {
    this.mostrarModalAsignar = false;
    this.envioSeleccionado = null;
    this.choferSeleccionado = '';
  }

  confirmarAsignacion() {
    if (!this.choferSeleccionado) {
      this.toastService.show('Debes seleccionar un chofer', 'warning');
      return;
    }

    this.guardando = true;

    const chofer = this.choferes?.find(c => c.id === this.choferSeleccionado);
    
    this.envioService.asignarChofer(this.envioSeleccionado.id, {
      choferId: this.choferSeleccionado,
      choferNombre: chofer?.nombre || chofer?.name || 'Chofer'
    }).subscribe({
      next: () => {
        this.toastService.success('Chofer asignado correctamente');
        this.guardando = false;
        this.cerrarModalAsignar();
        this.cargarEnvios();
      },
      error: (err: any) => {
        console.error('[EnviosAdmin] Error asignando chofer:', err);
        const message = getErrorMessage(err, 'al asignar el chofer');
        this.toastService.error(message);
        this.guardando = false;
        this.cdr.markForCheck();
      }
    });
  }

  async actualizarEstado(envio: any, nuevoEstado: string) {
    const confirmado = await this.confirmService.ask({
      title: 'Actualizar Estado',
      message: `¿Cambiar estado del envío #${envio.id} a ${nuevoEstado}?`,
      confirmText: 'Actualizar'
    });

    if (!confirmado) return;

    this.accionId = envio.id;
    this.cdr.markForCheck();

    this.trackingService.actualizarEstado(envio.id, nuevoEstado).subscribe({
      next: () => {
        this.toastService.success('Estado actualizado correctamente');
        this.accionId = null;
        this.cargarEnvios();
      },
      error: (err: any) => {
        console.error('[EnviosAdmin] Error actualizando estado:', err);
        const message = getErrorMessage(err, 'al actualizar el estado');
        this.toastService.error(message);
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }

  verTracking(code: string) {
    if (!code) {
      this.toastService.show('Sin código de tracking', 'warning');
      return;
    }

    this.router.navigate(['/tracking'], {
      queryParams: { codigo: code }
    });
  }

  puedeAsignarChofer(envio: any): boolean {
    return (envio.estado === 'PENDIENTE' || !envio.choferId);
  }
}
