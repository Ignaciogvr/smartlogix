import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

import { CarritoService } from '../../../core/services/carrito.service';

import { CurrencyClpPipe } from '../../../shared/pipes/currency.pipe';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    CurrencyClpPipe
  ],
  templateUrl: './carrito.html',
  styleUrls: ['./carrito.css']
})
export class CarritoComponent implements OnInit {
    private readonly cdr = inject(ChangeDetectorRef);
  productos: any[] = [];
  subtotal = 0;
  total = 0;

  constructor(
    private carritoService: CarritoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carritoService.carrito$.subscribe((items) => {
      this.productos = items;
      this.calcularTotales();
    });
  }

  eliminarProducto(id: number) {
    this.carritoService.eliminarProducto(id);
  }

  aumentarCantidad(id: number) {
    this.carritoService.aumentarCantidad(id);
  }

  disminuirCantidad(id: number) {
    this.carritoService.disminuirCantidad(id);
  }

  vaciarCarrito() {
    this.carritoService.vaciarCarrito();
  }

  calcularTotales() {
    this.subtotal = this.carritoService.obtenerSubtotal();
    this.total = this.carritoService.obtenerTotal();
  }

  irCheckout() {
    if (!this.productos.length) return;
    this.router.navigate(['/checkout']);
  }
}