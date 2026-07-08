import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EnvioService } from '../../../core/services/envio.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-dashboard-chofer',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardChoferComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private envioService = inject(EnvioService);
  private authService = inject(AuthService);

  loading = false;
  error = '';

  estadisticas = {
    pendientes: 0,
    enCamino: 0,
    entregados: 0,
    total: 0
  };

  enviosRecientes: any[] = [];

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.loading = true;
    this.error = '';

    const choferId = this.authService.getUserId();

    if (!choferId) {
      this.error = 'No se pudo identificar al chofer';
      this.loading = false;
      return;
    }

    this.envioService.obtenerPorChofer(choferId).subscribe({
      next: (envios: any[]) => {
        this.procesarEnvios(envios);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.error = 'Error al cargar los envíos asignados';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  getDeliveryRate(): number {
    if (this.estadisticas.total === 0) return 0;
    return Math.round((this.estadisticas.entregados / this.estadisticas.total) * 100);
  }

  private procesarEnvios(envios: any[]): void {
    this.estadisticas.total = envios.length;
    this.estadisticas.pendientes = envios.filter(e => e.estado === 'PENDIENTE').length;
    this.estadisticas.enCamino = envios.filter(e => e.estado === 'EN_CAMINO').length;
    this.estadisticas.entregados = envios.filter(e => e.estado === 'ENTREGADO').length;

    this.enviosRecientes = envios
      .sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime())
      .slice(0, 5);
  }
}
