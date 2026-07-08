import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError, BehaviorSubject, filter, take } from 'rxjs';

import { AuthService } from '../services/auth.service';

let isRefreshing = false;
let refreshTokenSubject = new BehaviorSubject<any>(null);

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const isAuthRoute = req.url.includes('/auth/');

      if (error.status === 401 && !isAuthRoute) {
        if (!isRefreshing) {
          isRefreshing = true;
          refreshTokenSubject.next(null);

          const refreshToken = localStorage.getItem('refresh_token');

          if (refreshToken) {
            return authService.refreshToken().pipe(
              switchMap(() => {
                isRefreshing = false;
                const token = localStorage.getItem('access_token');
                refreshTokenSubject.next(token);

                const retryReq = req.clone({
                  setHeaders: {
                    Authorization: `Bearer ${token}`
                  }
                });

                return next(retryReq);
              }),
              catchError((refreshError) => {
                console.warn('[ErrorInterceptor] Falló el refresh token, cerrando sesión');
                isRefreshing = false;
                authService.logout();
                window.location.href = '/';
                return throwError(() => refreshError);
              })
            );
          } else {
            console.warn(`[ErrorInterceptor] 401 recibido en ${req.url} sin refresh token. Cerrando sesión.`);
            isRefreshing = false;
            authService.logout();
            window.location.href = '/';
            return throwError(() => error);
          }
        } else {
          return refreshTokenSubject.pipe(
            filter(token => token !== null),
            take(1),
            switchMap(token => {
              const retryReq = req.clone({
                setHeaders: {
                  Authorization: `Bearer ${token}`
                }
              });
              return next(retryReq);
            })
          );
        }
      }

      return throwError(() => error);
    })
  );
};
