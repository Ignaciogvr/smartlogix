import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError, of } from 'rxjs';
import { BffService } from './bff.service';

@Injectable({ providedIn: 'root' })
export class UsuarioService {

  constructor(
    private http: HttpClient,
    private bff: BffService
  ) {}

  private get base(): string {
    return `${this.bff.url}/api/usuarios`;
  }

  // ─── Perfil propio (autenticado) ───────────────────────────

  /** GET /api/usuarios/me → obtener mi perfil */
  miPerfil(): Observable<any> {
    return this.http.get<any>(`${this.base}/me`).pipe(
      catchError(err => {
        console.error('[UsuarioService][miPerfil] Error:', err);
        return throwError(() => err);
      })
    );
  }

  /** POST /api/usuarios/me → registrar/sincronizar usuario tras login */
  registrarEnBff(): Observable<any> {
    return this.http.post<any>(`${this.base}/me`, {}).pipe(
      catchError(err => {
        console.warn('[UsuarioService][registrarEnBff] Error (puede ser que ya exista):', err);
        // 409 Conflict = ya existe → no es error real
        if (err?.status === 409 || err?.status === 201) {
          return of(null);
        }
        return of(null); // No bloquear el flujo de login por esto
      })
    );
  }

  /** PUT /api/usuarios/me → actualizar mi perfil */
  actualizarMiPerfil(data: any): Observable<any> {
    return this.http.put<any>(`${this.base}/me`, data).pipe(
      catchError(err => {
        console.error('[UsuarioService][actualizarMiPerfil] Error:', err);
        return throwError(() => err);
      })
    );
  }

  // ─── Gestión de usuarios (admin) ───────────────────────────

  listar(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}`).pipe(
      catchError(err => {
        console.error('[UsuarioService][listar] Error:', err);
        return throwError(() => err);
      })
    );
  }

  obtener(id: string): Observable<any> {
    return this.http.get<any>(`${this.base}/${id}`).pipe(
      catchError(err => {
        console.error(`[UsuarioService][obtener(${id})] Error:`, err);
        return throwError(() => err);
      })
    );
  }

  /** Alias de obtener() - compatibilidad con componentes existentes */
  obtenerUsuario(id: string): Observable<any> {
    // Si el id parece un auth0 sub (auth0|xxx), usar /api/usuarios/me
    // porque el backend no acepta el sub de Auth0 como ID numérico
    return this.miPerfil();
  }

  actualizar(id: string, data: any): Observable<any> {
    return this.http.put<any>(`${this.base}/${id}`, data).pipe(
      catchError(err => {
        console.error(`[UsuarioService][actualizar(${id})] Error:`, err);
        return throwError(() => err);
      })
    );
  }

  /** Alias de actualizarMiPerfil() para PerfilComponent */
  actualizarPerfil(id: string, data: any): Observable<any> {
    return this.actualizarMiPerfil(data);
  }

  activarUsuario(id: string): Observable<any> {
    return this.http.put<any>(`${this.base}/${id}/activar`, {}).pipe(
      catchError(err => throwError(() => err))
    );
  }

  desactivarUsuario(id: string): Observable<any> {
    return this.http.put<any>(`${this.base}/${id}/desactivar`, {}).pipe(
      catchError(err => throwError(() => err))
    );
  }

  suspenderUsuario(id: string, dias: number): Observable<any> {
    return this.http.put<any>(`${this.base}/${id}/suspender/${dias}`, {}).pipe(
      catchError(err => throwError(() => err))
    );
  }

  cambiarRol(id: string, rol: string): Observable<any> {
    return this.http.put<any>(`${this.base}/${id}/rol`, { rol }).pipe(
      catchError(err => throwError(() => err))
    );
  }

  crearVendedorOChofer(
    nombre: string,
    email: string,
    rol: string,
    documentoIdentidad: string
  ): Observable<any> {
    return this.http.post<any>(`${this.base}/crear`, {
      nombre,
      email,
      rol,
      documentoIdentidad
    }).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /** GET /api/usuarios/internal/{id} → validar existencia */
  validarExistencia(id: string): Observable<any> {
    return this.http.get<any>(`${this.base}/internal/${id}`).pipe(
      catchError(err => of(null))
    );
  }
}