import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AuthService } from '../../../core/services/auth.service';
import { UsuarioService } from '../../../core/services/usuario.service';

@Component({
  selector: 'app-callback',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './callback.html'
})
export class CallbackComponent implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  loading = true;
  error = '';
  statusMsg = 'Procesando tu inicio de sesión...';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {

      const code = params['code'];
      const state = params['state'];
      const errorParam = params['error'];
      const errorDescription = params['error_description'];

      // Auth0 devolvió un error
      if (errorParam) {
        this.loading = false;
        this.error = errorDescription || errorParam;
        console.error('[Callback] Error de Auth0:', errorParam, errorDescription);
        this.cdr.markForCheck();
        return;
      }

      // Debe venir el code
      if (!code || !state) {
        this.loading = false;
        this.error =
          'No se recibió código de autorización. Verifica la configuración de Auth0.';
        console.error('[Callback] Faltan parámetros code o state');
        this.cdr.markForCheck();
        return;
      }

      // Intercambiar code → access_token
      this.authService.handleCallback(code, state).subscribe({
        next: (user) => {
          console.log('[Callback] Login exitoso, usuario:', user);
          this.statusMsg = 'Sincronizando tu cuenta...';
          this.cdr.markForCheck();

          // Sincronizar usuario con el BFF (POST /api/usuarios/me)
          // Esto registra al usuario si es la primera vez, o lo actualiza si ya existe
          this.usuarioService.registrarEnBff().subscribe({
            next: () => {
              console.log('[Callback] Usuario sincronizado con BFF');
              this.redirigirSegunRol();
            },
            error: (err) => {
              // No bloquear el flujo por un error de sincronización
              console.warn('[Callback] No se pudo sincronizar con BFF (no bloquea login):', err);
              this.redirigirSegunRol();
            }
          });
        },
        error: (err) => {
          this.loading = false;
          this.error = this.authService.parseErrorMessage(err);
          console.error('[Callback] Error en handleCallback:', err);
          this.cdr.markForCheck();
        }
      });
    });
  }

  private redirigirSegunRol(): void {
    this.loading = false;
    this.cdr.markForCheck();

    // Obtener URL guardada antes del login
    const redirectUrl = localStorage.getItem('redirect_after_login');
    localStorage.removeItem('redirect_after_login');

    // Si hay URL guardada (no es callback), redirigir ahí
    if (redirectUrl && !redirectUrl.includes('/auth/callback')) {
      this.router.navigateByUrl(redirectUrl);
      return;
    }

    // Redirigir según el rol del usuario
    if (this.authService.isAdmin()) {
      this.router.navigate(['/admin/dashboard']);
    } else if (this.authService.isVendedor()) {
      this.router.navigate(['/vendedor/dashboard']);
    } else if (this.authService.isChofer()) {
      this.router.navigate(['/chofer/dashboard']);
    } else {
      this.router.navigate(['/']);
    }
  }

  volverAlInicio(): void {
    this.router.navigate(['/']);
  }
}
