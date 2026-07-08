import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../../core/services/admin.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { getErrorMessage, getErrorType, ApiErrorType } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private adminService = inject(AdminService);
  private usuarioService = inject(UsuarioService);
  private toastService = inject(ToastService);

  dashboard: any = null;
  usuariosRecientes: any[] = [];

  loading = false;
  loadingUsuarios = false;
  error = false;
  errorMessage = '';
  errorType: ApiErrorType | null = null;

  ngOnInit(): void {
    this.cargarDashboard();
    this.cargarUsuariosRecientes();
  }

  cargarDashboard(): void {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    this.errorType = null;

    this.adminService
      .dashboard()
      .subscribe({
        next: (response: any) => {
          // Siempre esperar un objeto con métricas, nunca asumir valores por defecto
          this.dashboard = response;
          this.loading = false;
          this.cdr.markForCheck();
        },
        error: (err: any) => {
          console.error('[DashboardAdmin] Error cargando dashboard:', err);
          
          this.errorType = getErrorType(err);
          this.errorMessage = getErrorMessage(err, 'al cargar el dashboard');
          this.error = true;
          this.loading = false;
          
          // Solo mostrar toast en errores de red o servidor, no en 404
          if (this.errorType === ApiErrorType.SERVER_ERROR || 
              this.errorType === ApiErrorType.NETWORK) {
            this.toastService.error(this.errorMessage);
          }
          
          this.cdr.markForCheck();
        }
      });
  }

  get isServerError(): boolean {
    return this.errorType === ApiErrorType.SERVER_ERROR;
  }

  get isNetworkError(): boolean {
    return this.errorType === ApiErrorType.NETWORK;
  }

  get isNotFoundError(): boolean {
    return this.errorType === ApiErrorType.NOT_FOUND;
  }

  cargarUsuariosRecientes(): void {
    this.loadingUsuarios = true;
    this.usuarioService.listar().subscribe({
      next: (usuarios: any) => {
        this.usuariosRecientes = (usuarios || []).slice(0, 5);
        this.loadingUsuarios = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingUsuarios = false;
        this.cdr.markForCheck();
      }
    });
  }
}