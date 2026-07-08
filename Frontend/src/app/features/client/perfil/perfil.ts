import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, finalize, of } from 'rxjs';

import { UsuarioService } from '../../../core/services/usuario.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../shared/components/toast/toast.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class PerfilComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private usuarioService = inject(UsuarioService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);

  perfil: {
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
    direccion: string;
  } = {
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    direccion: ''
  };

  rol = 'Cliente';
  estadoCuenta = 'Activa';
  iniciales = 'U';
  nombreCompleto = '';
  documentoIdentidad = '';

  loading = false;
  guardando = false;
  error = '';
  success = '';

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.loading = true;
    this.error = '';

    // Siempre usar GET /api/usuarios/me para obtener el perfil real
    this.usuarioService.miPerfil().pipe(
      catchError(err => {
        console.error('[PerfilComponent] Error cargando perfil desde BFF:', err);
        // Fallback: usar datos del token JWT
        const sessionUser = this.authService.getUser();
        return of({
          nombre: sessionUser?.nombre || sessionUser?.name || 'Usuario',
          apellido: '',
          email: sessionUser?.email || '',
          telefono: '',
          direccion: '',
          rol: sessionUser?.role || 'CLIENTE',
          estadoCuenta: 'Activa'
        });
      }),
      finalize(() => {
        this.loading = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: (response: any) => {
        this.mapearPerfil(response);
      }
    });
  }

  guardarPerfil(): void {
    this.guardando = true;
    this.error = '';
    this.success = '';

    const payload = {
      nombre: this.perfil.nombre,
      apellido: this.perfil.apellido,
      telefono: this.perfil.telefono,
      direccion: this.perfil.direccion
    };

    // Usar PUT /api/usuarios/me para actualizar el perfil
    this.usuarioService.actualizarMiPerfil(payload).pipe(
      catchError(err => {
        console.error('[PerfilComponent] Error guardando perfil:', err);
        return of(null);
      }),
      finalize(() => {
        this.guardando = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: (res) => {
        if (res !== null) {
          this.actualizarResumenPerfil();
          this.toastService.success('Perfil actualizado correctamente');
          this.success = 'Perfil actualizado correctamente';
        } else {
          this.toastService.error('No se pudo actualizar el perfil');
          this.error = 'No se pudo actualizar el perfil';
        }
      }
    });
  }

  private mapearPerfil(response: any): void {
    const sessionUser = this.authService.getUser();

    this.perfil = {
      nombre: response?.nombre || sessionUser?.nombre || sessionUser?.name || 'Usuario',
      apellido: response?.apellido || '',
      email: response?.email || sessionUser?.email || '',
      telefono: response?.telefono || '',
      direccion: response?.direccion || ''
    };

    this.documentoIdentidad = response?.documentoIdentidad || '';

    this.rol = response?.rol || response?.role || sessionUser?.role || 'CLIENTE';

    this.estadoCuenta = response?.estadoCuenta || response?.estado || 'Activa';

    this.actualizarResumenPerfil();
  }

  actualizarResumenPerfil(): void {
    const nombre = this.perfil.nombre?.trim() || 'Usuario';
    const apellido = this.perfil.apellido?.trim() || '';

    this.nombreCompleto = apellido
      ? `${nombre} ${apellido}`
      : nombre;

    this.iniciales = `${nombre.charAt(0)}${apellido.charAt(0) || ''}`
      .toUpperCase()
      .slice(0, 2);
  }
}
