import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { PedidoService } from '../../../core/services/pedido.service';
import { TrackingService } from '../../../core/services/tracking.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';
import { getErrorMessage, getErrorType, ApiErrorType } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-tracking',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './tracking.html',
  styleUrl: './tracking.css'
})
export class TrackingComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private pedidoService = inject(PedidoService);
  private trackingService = inject(TrackingService);
  private route = inject(ActivatedRoute);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);

  estadoCompleto: any = null;

  // Campo de búsqueda por código
  codigoBusqueda = '';

  loading = false;
  error = '';
  errorType: ApiErrorType | null = null;
  cancelando = false;

  /**
   * Pasos del ciclo de vida del PEDIDO para la barra principal.
   */
  pasosPedido = [
    { key: 'CONFIRMADO', label: 'Confirmado',  color: 'var(--primary)' }, // Azul
    { key: 'PREPARANDO', label: 'Preparando',  color: 'var(--warning)' }, // Naranjo
    { key: 'ENVIADO',    label: 'Enviado',     color: '#8a2be2' },        // Morado
    { key: 'EN_CAMINO',  label: 'En camino',   color: '#00ced1' },        // Celeste/Turquesa
    { key: 'ENTREGADO',  label: 'Entregado',   color: 'var(--success)' }  // Verde
  ];

  /**
   * Pasos del ciclo de vida de un ENVÍO (Timeline detallado)
   */
  pasos = [
    { key: 'PENDIENTE',       label: 'Recibido' },
    { key: 'EN_PREPARACION',  label: 'En preparación' },
    { key: 'ENVIADO',         label: 'Despachado' },
    { key: 'ENTREGADO',       label: 'Entregado' }
  ];

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const pedidoId = params['pedidoId'];
      const codigo = params['codigo'];

      if (pedidoId) {
        this.buscarEstadoPedido(Number(pedidoId));
      } else if (codigo) {
        this.codigoBusqueda = codigo;
        this.buscarPorCodigo(codigo);
      } else {
        this.error = 'No se proporcionó un ID de pedido o código de tracking.';
      }
    });
  }

  /** Búsqueda por ID de pedido → usa /api/v1/pedidos/{id}/estado-completo */
  buscarEstadoPedido(pedidoId: number): void {
    this.loading = true;
    this.error = '';
    this.errorType = null;
    this.estadoCompleto = null;

    this.pedidoService.estadoPedidoCompleto(pedidoId).subscribe({
      next: (response) => {
        this.estadoCompleto = response;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[Tracking] Error obteniendo estado del pedido:', err);
        this.errorType = getErrorType(err);
        this.error = getErrorMessage(err, 'al obtener el estado del pedido');
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  /** Búsqueda por código de tracking → usa /api/tracking/codigo/{code} */
  buscarPorCodigo(codigo: string): void {
    if (!codigo?.trim()) return;
    this.loading = true;
    this.error = '';
    this.errorType = null;
    this.estadoCompleto = null;

    this.trackingService.obtenerPorCodigo(codigo.trim()).subscribe({
      next: (response) => {
        this.estadoCompleto = response;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[Tracking] Error buscando por código:', err);
        this.errorType = getErrorType(err);
        this.error = getErrorMessage(err, 'al buscar el tracking');
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  buscarManual(): void {
    if (this.codigoBusqueda.trim()) {
      // Intentar como número (pedidoId) o como código
      const comoNumero = Number(this.codigoBusqueda);
      if (!isNaN(comoNumero) && comoNumero > 0) {
        this.buscarEstadoPedido(comoNumero);
      } else {
        this.buscarPorCodigo(this.codigoBusqueda);
      }
    }
  }

  async cancelarPedido(pedidoId: number): Promise<void> {
    const confirmar = await this.confirmService.ask({
      title: 'Cancelar Pedido',
      message: '¿Estás seguro de que deseas cancelar tu pedido?',
      confirmText: 'Sí, cancelar',
      cancelText: 'No',
      isDanger: true
    });

    if (!confirmar) return;

    this.cancelando = true;
    this.cdr.markForCheck();

    this.pedidoService.cancelarPedido(pedidoId).subscribe({
      next: () => {
        this.toastService.success('Pedido cancelado correctamente');
        this.cancelando = false;
        this.buscarEstadoPedido(pedidoId);
      },
      error: (err) => {
        console.error('[Tracking] Error cancelando pedido:', err);
        const message = getErrorMessage(err, 'al cancelar el pedido');
        this.toastService.error(message);
        this.cancelando = false;
        this.cdr.markForCheck();
      }
    });
  }

  /**
   * Devuelve el índice del paso activo para la barra principal del pedido.
   */
  obtenerPasoPedidoActivo(estado?: string): number {
    if (!estado) return -1;
    const upper = estado.toUpperCase();
    if (upper === 'PENDIENTE') return -1; // Antes de confirmar
    const idx = this.pasosPedido.findIndex(p => p.key === upper || upper.includes(p.key));
    return idx;
  }

  isCancelado(estado?: string): boolean {
    if (!estado) return false;
    return estado.toUpperCase() === 'CANCELADO';
  }

  /**
   * Devuelve el índice del paso activo (0-based) en la línea de tiempo del envío.
   */
  obtenerPasoActivo(estado?: string): number {
    if (!estado) return 0;
    const upper = estado.toUpperCase();
    const idx = this.pasos.findIndex(p => p.key === upper || upper.includes(p.key));
    return idx >= 0 ? idx : 0;
  }

  /**
   * Agrupa los items del pedido por vendedor para mostrarlos
   * junto al envío correspondiente.
   */
  itemsPorVendedor(vendedorId: string): any[] {
    if (!this.estadoCompleto?.items) return [];
    return this.estadoCompleto.items.filter((i: any) => i.vendedorId === vendedorId);
  }
}
