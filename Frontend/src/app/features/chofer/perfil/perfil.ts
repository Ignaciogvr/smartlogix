import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../../core/services/usuario.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-perfil-chofer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css']
})
export class PerfilChoferComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private usuarioService = inject(UsuarioService);
  private authService = inject(AuthService);

  loading = false;
  error = '';
  success = '';

  perfil: any = {
    nombre: '',
    email: '',
    telefono: '',
    direccion: ''
  };

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.loading = true;
    this.error = '';

    this.usuarioService.miPerfil().subscribe({
      next: (data: any) => {
        this.perfil = data || {};
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[PerfilChofer] Error cargando perfil:', err);
        this.error = 'Error al cargar tu perfil';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  guardarPerfil(): void {
    this.loading = true;
    this.error = '';
    this.success = '';

    this.usuarioService.actualizarMiPerfil(this.perfil).subscribe({
      next: () => {
        this.success = 'Perfil actualizado correctamente';
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[PerfilChofer] Error guardando perfil:', err);
        this.error = 'Error al actualizar el perfil';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }
}
