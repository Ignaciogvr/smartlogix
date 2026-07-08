import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard = (role: string): CanActivateFn => {
  return (route, state) => {
    const router = inject(Router);
    const authService = inject(AuthService);

    if (!authService.isAuthenticated()) {
      localStorage.setItem('redirect_after_login', state.url);
      authService.login().subscribe();
      return false;
    }

    if (!authService.hasRole(role)) {
      router.navigate(['/unauthorized']);
      return false;
    }

    return true;
  };
};