import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../../core/services/usuario.service';
import { ToastService } from '../../../shared/components/toast/toast.service';
import { ConfirmService } from '../../../shared/components/confirm/confirm.service';
import { getErrorMessage, getErrorType, ApiErrorType, isEmptyResponse } from '../../../shared/models/api-error.model';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios.html',
  styleUrls: ['./usuarios.css']
})
export class UsuariosComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private toastService = inject(ToastService);
  private confirmService = inject(ConfirmService);
  private usuarioService = inject(UsuarioService);

  usuarios: any[] | null = null;
  loading = false;
  error = false;
  errorMessage = '';
  errorType: ApiErrorType | null = null;
  accionId: string | null = null;

  mostrarModalCrear = false;
  creando = false;
  nuevoUsuario: any = {
    rol: 'VENDEDOR',
    nombre: '',
    email: '',
    documentoIdentidad: '',
    fotoDocumento: null
  };

  ngOnInit(): void {
    this.cdr.markForCheck();
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    this.errorType = null;

    this.usuarioService.listar().subscribe({
      next: (response: any) => {
        console.log('[UsuariosComponent] Response recibido:', response);
        // El endpoint devuelve ApiResponse { code, message, data }
        // Extraemos el array de usuarios del campo data
        this.usuarios = (response?.data || response) as any[];
        console.log('[UsuariosComponent] Usuarios cargados:', this.usuarios);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('[UsuariosComponent] Error cargando usuarios:', err);
        
        this.errorType = getErrorType(err);
        this.errorMessage = getErrorMessage(err, 'al cargar los usuarios');
        this.error = true;
        this.loading = false;
        
        this.toastService.error(this.errorMessage);
        this.cdr.markForCheck();
      }
    });
  }

  get hayUsuarios(): boolean {
    return this.usuarios !== null && this.usuarios.length > 0;
  }

  get sinDatos(): boolean {
    return !this.loading && !this.error && this.usuarios !== null && this.usuarios.length === 0;
  }

  async desactivar(id: string, nombre: string) {
    const confirmado = await this.confirmService.ask({
      title: 'Desactivar Usuario',
      message: `¿Estás seguro de desactivar al usuario "${nombre}"?`,
      confirmText: 'Desactivar',
      isDanger: true
    });

    if (!confirmado) return;

    this.accionId = id;
    this.cdr.markForCheck();

    this.usuarioService.desactivarUsuario(id).subscribe({
      next: () => {
        this.toastService.success('Usuario desactivado correctamente');
        this.accionId = null;
        this.cargarUsuarios();
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('[UsuariosComponent] Error desactivando usuario:', err);
        const message = getErrorMessage(err, 'al desactivar el usuario');
        this.toastService.error(message);
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }

  async activar(id: string, nombre: string) {
    const confirmado = await this.confirmService.ask({
      title: 'Activar Usuario',
      message: `¿Estás seguro de reactivar al usuario "${nombre}"?`,
      confirmText: 'Activar'
    });

    if (!confirmado) return;

    this.accionId = id;
    this.cdr.markForCheck();

    this.usuarioService.activarUsuario(id).subscribe({
      next: () => {
        this.toastService.success('Usuario activado correctamente');
        this.accionId = null;
        this.cargarUsuarios();
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('[UsuariosComponent] Error activando usuario:', err);
        const message = getErrorMessage(err, 'al activar el usuario');
        this.toastService.error(message);
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }

  async timeoutUsuario(id: string, nombre: string, dias: number) {
    const confirmado = await this.confirmService.ask({
      title: `Suspender Usuario ${dias} días`,
      message: `¿Suspender a "${nombre}" por ${dias} días?`,
      confirmText: 'Suspender',
      isDanger: true
    });

    if (!confirmado) return;

    this.accionId = id;
    this.cdr.markForCheck();

    this.usuarioService.suspenderUsuario(id, dias).subscribe({
      next: () => {
        this.toastService.success(`Usuario suspendido por ${dias} días`);
        this.accionId = null;
        this.cargarUsuarios();
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('[UsuariosComponent] Error aplicando timeout:', err);
        const message = getErrorMessage(err, 'al suspender el usuario');
        this.toastService.error(message);
        this.accionId = null;
        this.cdr.markForCheck();
      }
    });
  }

  abrirModalCrear(): void {
    this.mostrarModalCrear = true;
    this.nuevoUsuario = {
      rol: 'VENDEDOR',
      nombre: '',
      email: '',
      documentoIdentidad: '',
      fotoDocumento: null
    };
  }

  cerrarModalCrear(): void {
    this.mostrarModalCrear = false;
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.nuevoUsuario.fotoDocumento = file;
    }
  }

  crearUsuario(): void {
    this.creando = true;
    this.cdr.markForCheck();

    const usuarioData = {
      nombre: this.nuevoUsuario.nombre,
      email: this.nuevoUsuario.email,
      rol: this.nuevoUsuario.rol,
      documentoIdentidad: this.nuevoUsuario.documentoIdentidad
    };

    this.usuarioService.crearVendedorOChofer(
      usuarioData.nombre,
      usuarioData.email,
      usuarioData.rol,
      usuarioData.documentoIdentidad
    ).subscribe({
      next: () => {
        this.toastService.success(`Usuario ${this.nuevoUsuario.rol.toLowerCase()} creado correctamente`);
        this.creando = false;
        this.cerrarModalCrear();
        this.cargarUsuarios();
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('[UsuariosComponent] Error creando usuario:', err);
        const message = getErrorMessage(err, 'al crear el usuario');
        this.toastService.error(message);
        this.creando = false;
        this.cdr.markForCheck();
      }
    });
  }
}
