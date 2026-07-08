import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';

import { CatalogoService } from '../../../core/services/catalogo.service';
import { AdminService } from '../../../core/services/admin.service';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [
    CommonModule,
    CurrencyPipe
  ],
  templateUrl: './inventario.html',
  styleUrls: ['./inventario.css']
})
export class InventarioComponent implements OnInit {
    private readonly cdr = inject(ChangeDetectorRef);
productos: any[] = [];
  loading = false;

  constructor(
    private catalogoService: CatalogoService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.cargarInventario();
  }

  cargarInventario() {
    this.loading = true;

    this.catalogoService.listarProductos().subscribe({
      next: (data: any) => {
        this.productos = data;
        this.loading = false;
            this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('Error inventario BFF:', err);
        this.loading = false;
          this.cdr.markForCheck();
      }
    });
  }

  recargarDashboardAdmin() {
    this.adminService.dashboardAdmin().subscribe({
      next: (res: any) => {
        console.log('Dashboard actualizado:', res);
            this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error(err);
          this.cdr.markForCheck();
      }
    });
  }
}