import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

import { PedidoService } from '../../../core/services/pedido.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';
import { HttpErrorResponse } from '@angular/common/http';
import { getErrorType, getErrorMessage, ApiErrorType } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './pedidos.html',
  styleUrl: './pedidos.css'
})
export class PedidosComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private pedidoService = inject(PedidoService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);
  private router = inject(Router);

  pedidos: any[] | null = null;
  loading = false;
  accionId: number | null = null;
  errorType: ApiErrorType | null = null;
  errorMessage = '';

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.loading = true;
    this.errorType = null;
    this.errorMessage = '';

    // GET /api/pedidos/mis-pedidos → pedidos del usuario autenticado
    this.pedidoService.listar().subscribe({
      next: (response) => {
        this.pedidos = (response as any[]) || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err: HttpErrorResponse) => {
        console.error('[PedidosComponent] Error al cargar pedidos:', err);
        this.errorType = getErrorType(err);
        this.errorMessage = getErrorMessage(err);
        this.pedidos = null;
        this.loading = false;

        if (this.errorType === ApiErrorType.UNAUTHORIZED) {
          this.toastService.error('Sesión expirada. Por favor inicia sesión nuevamente.');
        } else if (this.errorType === ApiErrorType.SERVER_ERROR) {
          this.toastService.error('Error del servidor. Por favor intenta más tarde.');
        } else {
          this.toastService.error('No se pudieron cargar los pedidos');
        }

        this.cdr.markForCheck();
      }
    });
  }

  async cancelarPedido(id: number): Promise<void> {
    const confirmar = await this.confirmService.ask({
      title: 'Cancelar Pedido',
      message: '¿Estás seguro de que deseas cancelar este pedido?',
      confirmText: 'Sí, cancelar',
      cancelText: 'No',
      isDanger: true
    });

    if (!confirmar) return;

    this.accionId = id;
    this.cdr.markForCheck();

    this.pedidoService.cancelarPedido(id).subscribe({
      next: () => {
        this.toastService.success('Pedido cancelado correctamente');
        this.accionId = null;
        this.cargarPedidos();
      },
      error: (err) => {
        console.error(`[PedidosComponent] Error al intentar cancelar pedido #${id}:`, err);
        this.toastService.error('No se pudo cancelar el pedido');
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }

  verTracking(pedido: any): void {
    this.router.navigate(['/tracking'], {
      queryParams: { pedidoId: pedido.id }
    });
  }

  puedeCancelar(estado: string): boolean {
    const valor = estado?.toUpperCase() || '';
    return valor === 'PENDIENTE' || valor === 'CONFIRMADO' || valor === 'EN_PROCESO';
  }
}

