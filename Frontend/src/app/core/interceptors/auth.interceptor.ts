import { HttpInterceptorFn } from '@angular/common/http';

const PUBLIC_AUTH_PATHS = [
  '/auth/login',
  '/auth/register',
  '/auth/refresh'
];

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const isPublicAuth = PUBLIC_AUTH_PATHS.some((path) =>
    req.url.includes(path)
  );

  if (isPublicAuth) {
    return next(req);
  }

  const token = localStorage.getItem('access_token');

  if (!token) {
    return next(req);
  }

  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(authReq);
};
