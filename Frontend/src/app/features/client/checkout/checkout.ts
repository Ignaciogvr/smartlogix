import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { CarritoService } from '../../../core/services/carrito.service';
import { PedidoService } from '../../../core/services/pedido.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../shared/components/toast/toast.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class CheckoutComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private carritoService = inject(CarritoService);
  private pedidoService = inject(PedidoService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  carrito: any[] = [];
  loading = false;

  // Campos de dirección mejorados
  direccion = '';
  departamento = '';
  comuna = '';
  ciudad = 'Santiago';
  telefono = '';
  notasEntrega = '';
  metodoPago = 'tarjeta';

  // Lista de comunas de Santiago (para el selector)
  comunas = [
    'Cerrillos', 'Cerro Navia', 'Conchalí', 'El Bosque', 'Estación Central',
    'Huechuraba', 'Independencia', 'La Cisterna', 'La Florida', 'La Granja',
    'La Pintana', 'La Reina', 'Las Condes', 'Lo Barnechea', 'Lo Espejo',
    'Lo Prado', 'Macul', 'Maipú', 'Ñuñoa', 'Pedro Aguirre Cerda',
    'Peñalolén', 'Providencia', 'Pudahuel', 'Quilicura', 'Quinta Normal',
    'Recoleta', 'Renca', 'San Joaquín', 'San Miguel', 'San Ramón',
    'Santiago Centro', 'Vitacura'
  ];

  total = 0;

  ngOnInit(): void {
    this.carritoService.carrito$.subscribe(items => {
      this.carrito = items;
      this.calcularTotal();
      this.cdr.markForCheck();
    });
  }

  calcularTotal(): void {
    this.total = this.carrito.reduce(
      (sum, item) => sum + (item.precio * item.cantidad),
      0
    );
  }

  formularioValido(): boolean {
    return !!(
      this.direccion?.trim() &&
      this.comuna?.trim() &&
      this.telefono?.trim()
    );
  }

  confirmarCompra(): void {
    if (!this.formularioValido()) {
      this.toastService.show('Completa todos los campos obligatorios', 'warning');
      return;
    }

    const usuarioId = this.authService.getUserId();

    if (!usuarioId) {
      this.toastService.show('Debes iniciar sesión para comprar', 'warning');
      this.authService.login().subscribe();
      return;
    }

    // Construir dirección completa
    let direccionCompleta = this.direccion;
    if (this.departamento) {
      direccionCompleta += `, ${this.departamento}`;
    }
    direccionCompleta += `, ${this.comuna}, ${this.ciudad}`;

    const payload = {
      usuarioId,
      productos: this.carrito.map(item => ({
        productoId: item.id,
        cantidad: item.cantidad,
        precio: item.precio,
        nombre: item.nombre
      })),
      direccionEnvio: direccionCompleta,
      ciudadEnvio: this.ciudad,
      comunaEnvio: this.comuna,
      metodoPago: this.metodoPago,
      telefono: this.telefono,
      notasEntrega: this.notasEntrega || undefined
    };

    this.loading = true;

    // Generar Idempotency-Key
    const idempotencyKey = crypto.randomUUID();

    this.pedidoService.checkout(payload, idempotencyKey)
      .subscribe({
        next: () => {
          this.carritoService.limpiarCarrito();
          this.toastService.success('¡Compra realizada con éxito!');
          this.router.navigate(['/pedidos']);
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('[CheckoutComponent] Error al procesar checkout:', err);
          
          let errorMessage = 'Error al procesar compra';
          if (err?.error?.message) {
            errorMessage = err.error.message;
          } else if (err?.error?.error) {
            errorMessage = err.error.error;
          } else if (err?.message) {
            errorMessage = err.message;
          }
          
          this.toastService.error(errorMessage);
          this.loading = false;
          this.cdr.markForCheck();
        },
        complete: () => {
          this.loading = false;
        }
      });
  }
}