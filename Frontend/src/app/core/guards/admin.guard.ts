import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  if (!authService.isAuthenticated()) {
    // Guardar la URL a la que intentaba acceder
    localStorage.setItem('redirect_after_login', state.url);
    
    // Redirigir a login cuando intente acceder a admin sin autenticación
    authService.login().subscribe();
    return false;
  }

  if (!authService.isAdmin()) {
    // Si está autenticado pero no es admin, mostrar unauthorized
    router.navigate(['/unauthorized']);
    return false;
  }

  return true;
};
