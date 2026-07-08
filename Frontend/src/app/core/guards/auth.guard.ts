import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    // Guardar la URL a la que intentaba acceder
    localStorage.setItem('redirect_after_login', state.url);
    
    // Redirigir a login cuando intente acceder a ruta protegida
    authService.login().subscribe();
    return false;
  }

  return true;
};
