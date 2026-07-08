import { HttpInterceptorFn } from '@angular/common/http';
import { finalize } from 'rxjs';

let requests = 0;

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  requests++;
  document.body.classList.add('loading');

  return next(req).pipe(
    finalize(() => {
      requests--;
      if (requests === 0) {
        document.body.classList.remove('loading');
      }
    })
  );
};