import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';

import { AdminService } from '../../../core/services/admin.service';
import { PedidoService } from '../../../core/services/pedido.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';
import { HttpErrorResponse } from '@angular/common/http';
import { getErrorType, getErrorMessage, ApiErrorType } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-pedidos-admin',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './pedidos-admin.html',
  styleUrls: ['./pedidos-admin.css']
})
export class PedidosAdminComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private adminService = inject(AdminService);
  private pedidoService = inject(PedidoService);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);

  pedidos: any[] | null = null;
  loading = false;
  accionId: number | null = null;
  errorType: ApiErrorType | null = null;
  errorMessage = '';

  // Modal Detalle
  mostrarModal = false;
  pedidoSeleccionado: any = null;
  loadingDetalle = false;

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.loading = true;
    this.errorType = null;
    this.errorMessage = '';

    this.adminService.listarTodosPedidos().subscribe({
      next: (data: any) => {
        this.pedidos = data || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error BFF pedidos:', err);
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

  verDetalle(id: number): void {
    this.mostrarModal = true;
    this.loadingDetalle = true;
    this.pedidoSeleccionado = null;

    this.pedidoService.estadoPedidoCompleto(id).subscribe({
      next: (data) => {
        this.pedidoSeleccionado = data;
        this.loadingDetalle = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error cargando detalle del pedido', err);
        this.toastService.error('Error al cargar el detalle del pedido');
        this.loadingDetalle = false;
        this.cdr.markForCheck();
      }
    });
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.pedidoSeleccionado = null;
  }

  async cambiarEstado(id: number, estadoDestino: string): Promise<void> {
    const isCancel = estadoDestino === 'CANCELADO';
    
    const confirmado = await this.confirmService.ask({
      title: isCancel ? 'Cancelar Pedido' : 'Actualizar Estado',
      message: `¿Estás seguro de cambiar el pedido #${id} a ${estadoDestino}?`,
      confirmText: isCancel ? 'Cancelar Pedido' : 'Actualizar',
      isDanger: isCancel
    });

    if (!confirmado) return;

    this.accionId = id;
    this.cdr.markForCheck();

    let req;
    if (estadoDestino === 'PREPARANDO' || estadoDestino === 'EN_PROCESO') {
      req = this.adminService.preparar(id);
    } else if (estadoDestino === 'ENVIADO') {
      req = this.adminService.enviarPedido(id);
    } else if (estadoDestino === 'ENTREGADO') {
      req = this.adminService.entregarPedido(id);
    } else if (estadoDestino === 'CANCELADO') {
      req = this.pedidoService.cancelarPedido(id);
    } else {
      this.accionId = null;
      return;
    }

    req.subscribe({
      next: () => {
        this.toastService.success('Estado del pedido actualizado');
        this.accionId = null;
        this.cargarPedidos();
        if (this.mostrarModal && this.pedidoSeleccionado?.pedidoId === id) {
          this.verDetalle(id); // Reload modal details
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error cambiando estado', err);
        this.toastService.error('Error al cambiar el estado del pedido');
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }
}