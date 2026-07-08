import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

import { environment } from '../../../environments/environment';

import {
  AuthTokenResponse,
  LoginCredentials,
  RegisterCredentials,
  SessionUser
} from '../../shared/interfaces/auth.interface';

const ROLES_CLAIM = 'https://smartlogix.com/roles';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly userKey = 'user';
  private readonly expiresKey = 'token_expires_at';

  constructor(
    private http: HttpClient
  ) {
    // Al iniciar, restaurar sesión desde token guardado
    this.restoreSession();
  }

  // ─── Auth0 Authorization Code + PKCE ───────────────────

  /**
   * Redirige al Universal Login de Auth0 con PKCE.
   * Se llama desde login() y register().
   */
  private async redirectToAuth0(screenHint?: string): Promise<void> {
    // Validar que estamos en el browser (no SSR)
    if (typeof window === 'undefined' || typeof crypto === 'undefined') {
      console.error('[AuthService] No se puede redirigir a Auth0 fuera del browser');
      return;
    }

    const { domain, clientId, audience, redirectUri } = environment.auth;

    // Generar code_verifier (random 43-128 chars)
    const codeVerifier = this.generateCodeVerifier();
    localStorage.setItem('pkce_code_verifier', codeVerifier);

    // Generar code_challenge (SHA-256 del verifier, base64url)
    const codeChallenge = await this.generateCodeChallenge(codeVerifier);

    // State para CSRF protection
    const state = this.generateRandomString(32);
    localStorage.setItem('auth0_state', state);

    const params = new URLSearchParams({
      response_type: 'code',
      client_id: clientId,
      redirect_uri: redirectUri,
      audience: audience,
      scope: 'openid profile email offline_access',
      state: state,
      code_challenge: codeChallenge,
      code_challenge_method: 'S256'
    });

    if (screenHint) {
      params.set('screen_hint', screenHint);
    }

    window.location.href =
      `https://${domain}/authorize?${params.toString()}`;
  }

  /**
   * Intercambia el authorization code por access_token.
   * Se llama desde CallbackComponent.
   */
  handleCallback(code: string, state: string): Observable<SessionUser> {
    const { domain, clientId, redirectUri } = environment.auth;

    // Verificar state (CSRF)
    const savedState = localStorage.getItem('auth0_state');
    if (state !== savedState) {
      localStorage.removeItem('auth0_state');
      localStorage.removeItem('pkce_code_verifier');
      return throwError(() => new Error('State inválido. Posible ataque CSRF.'));
    }

    const codeVerifier = localStorage.getItem('pkce_code_verifier');
    if (!codeVerifier) {
      localStorage.removeItem('auth0_state');
      return throwError(() => new Error('Code verifier no encontrado.'));
    }

    // Limpiar state y verifier
    localStorage.removeItem('auth0_state');
    localStorage.removeItem('pkce_code_verifier');

    const body = new HttpParams()
      .set('grant_type', 'authorization_code')
      .set('client_id', clientId)
      .set('code', code)
      .set('code_verifier', codeVerifier)
      .set('redirect_uri', redirectUri);

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/x-www-form-urlencoded');

    return this.http
      .post<any>(
        `https://${domain}/oauth/token`,
        body.toString(),
        { headers }
      )
      .pipe(
        map((response) => {
          if (!response.access_token) {
            throw new Error('No se recibió access_token de Auth0');
          }

          const accessToken = response.access_token;
          const expiresIn = response.expires_in || 3600;

          // Guardar token en localStorage
          localStorage.setItem('access_token', accessToken);

          if (response.refresh_token) {
            localStorage.setItem('refresh_token', response.refresh_token);
          }

          // Calcular expiración
          const expiresAt = Date.now() + (expiresIn * 1000);
          localStorage.setItem(this.expiresKey, expiresAt.toString());

          // Extraer usuario del JWT
          const user = this.userFromToken(accessToken);
          if (!user) {
            throw new Error('No se pudo extraer información del usuario del token');
          }

          localStorage.setItem(this.userKey, JSON.stringify(user));

          return user;
        }),
        catchError((err) => {
          console.error('[AuthService] Error intercambiando code por token:', err);
          this.clearSession();
          return throwError(() => err);
        })
      );
  }

  // ─── Login / Register / Logout ─────────────────────────

  /**
   * Redirige al login de Auth0.
   * El formulario local queda como fallback visual,
   * pero la acción real es redirigir a Auth0.
   */
  login(
    credentials?: LoginCredentials
  ): Observable<SessionUser> {
    // Redirige a Auth0 Universal Login (manejo async correcto)
    this.redirectToAuth0().catch(err => {
      console.error('[AuthService] Error al iniciar flujo de login:', err);
    });

    // Retorna observable que nunca emite
    // (la página se va a redirigir)
    return new Observable<SessionUser>(() => {});
  }

  register(
    credentials?: RegisterCredentials
  ): Observable<SessionUser> {
    // Redirige a Auth0 con pantalla de signup (manejo async correcto)
    this.redirectToAuth0('signup').catch(err => {
      console.error('[AuthService] Error al iniciar flujo de registro:', err);
    });

    return new Observable<SessionUser>(() => {});
  }

  refreshToken(): Observable<SessionUser> {
    const refreshToken = localStorage.getItem('refresh_token');

    if (!refreshToken) {
      return throwError(
        () => new Error('No hay refresh token')
      );
    }

    const { domain, clientId } = environment.auth;

    const body = new HttpParams()
      .set('grant_type', 'refresh_token')
      .set('client_id', clientId)
      .set('refresh_token', refreshToken);

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/x-www-form-urlencoded');

    return this.http
      .post<any>(
        `https://${domain}/oauth/token`,
        body.toString(),
        { headers }
      )
      .pipe(
        map((response) => {
          const accessToken = response.access_token;
          const expiresIn = response.expires_in || 3600;

          localStorage.setItem('access_token', accessToken);

          if (response.refresh_token) {
            localStorage.setItem('refresh_token', response.refresh_token);
          }

          const expiresAt = Date.now() + (expiresIn * 1000);
          localStorage.setItem(this.expiresKey, expiresAt.toString());

          const user = this.userFromToken(accessToken);
          if (user) {
            localStorage.setItem(this.userKey, JSON.stringify(user));
          }

          return user!;
        }),
        catchError((err) => {
          this.clearSession();
          return throwError(() => err);
        })
      );
  }

  validateSession(): Observable<boolean> {
    return of(this.isAuthenticated() && !this.isTokenExpired());
  }

  logout(): void {
    const { domain, clientId } = environment.auth;
    this.clearSession();

    // Redirigir a Auth0 logout (fix: redirige a home, no a /auth/login que no existe)
    const returnTo = encodeURIComponent(window.location.origin);
    window.location.href =
      `https://${domain}/v2/logout?client_id=${clientId}&returnTo=${returnTo}`;
  }

  // ─── Token / Session helpers ───────────────────────────

  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    return !this.isTokenExpired();
  }

  isTokenExpired(): boolean {
    const expiresAt = localStorage.getItem(this.expiresKey);
    if (!expiresAt) {
      // Si no hay expiresAt pero hay token, intentar leerlo del JWT
      const token = this.getToken();
      if (token) {
        try {
          const decoded: any = jwtDecode(token);
          if (decoded.exp) {
            const expMs = decoded.exp * 1000;
            localStorage.setItem(this.expiresKey, expMs.toString());
            return Date.now() > expMs;
          }
        } catch {
          return true;
        }
      }
      return true;
    }
    return Date.now() > parseInt(expiresAt, 10);
  }

  getUser(): SessionUser | null {
    const raw = localStorage.getItem(this.userKey);

    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as SessionUser;
    } catch {
      return null;
    }
  }

  getRole(): string {
    return this.getUser()?.role ?? 'CLIENTE';
  }

  hasRole(role: string): boolean {
    const user = this.getUser();

    if (!user) {
      return false;
    }

    const roles = [
      ...(user.roles ?? []),
      ...(user.permissions ?? []),
      user.role ?? ''
    ].map((r) => r.toUpperCase());

    return roles.includes(role.toUpperCase());
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isVendedor(): boolean {
    return this.hasRole('VENDEDOR');
  }

  isChofer(): boolean {
    return this.hasRole('CHOFER');
  }

  /**
   * Retorna la URL base del BFF para sincronización de usuario.
   * Importada desde environment para no crear dependencia circular.
   */
  getBffUrl(): string {
    return environment.api.bff;
  }

  getUserId(): string | null {
    return this.getUser()?.sub ?? null;
  }

  setTokensFromCallback(
    accessToken: string,
    refreshToken?: string
  ): void {
    localStorage.setItem('access_token', accessToken);

    if (refreshToken) {
      localStorage.setItem('refresh_token', refreshToken);
    }

    // Calcular expiración del JWT
    try {
      const decoded: any = jwtDecode(accessToken);
      if (decoded.exp) {
        localStorage.setItem(
          this.expiresKey,
          (decoded.exp * 1000).toString()
        );
      }
    } catch (error) {
      // Si no se puede decodificar el token, loguear error pero no hacer logout
      // (el logout es muy agresivo, mejor dejar que falle después si es necesario)
      console.error('[AuthService] Error decodificando token JWT:', error);
    }

    const user = this.userFromToken(accessToken);
    if (user) {
      localStorage.setItem(this.userKey, JSON.stringify(user));
    }
  }

  parseErrorMessage(error: unknown): string {

    if (error instanceof HttpErrorResponse) {

      return (
        error.error?.message ||
        error.error?.error_description ||
        error.message ||
        'Error de autenticación'
      );
    }

    if (error instanceof Error) {
      return error.message;
    }

    return 'Error de autenticación';
  }

  // ─── Private helpers ───────────────────────────────────

  /**
   * Restaura sesión al iniciar la app si hay token válido en localStorage.
   * SOLO restaura si hay token REAL de Auth0, NO simula ninguna sesión.
   */
  private restoreSession(): void {
    const token = localStorage.getItem('access_token');
    
    // NO CONTINUAR si no hay token real
    if (!token) {
      this.clearSession();
      return;
    }

    // Verificar que el token sea válido y no esté expirado
    if (this.isTokenExpired()) {
      console.log('[AuthService] Token expirado, limpiando sesión');
      this.clearSession();
      return;
    }

    // Verificar que el token sea un JWT válido
    try {
      const decoded: any = jwtDecode(token);
      if (!decoded.sub || !decoded.exp) {
        console.warn('[AuthService] Token inválido, limpiando sesión');
        this.clearSession();
        return;
      }
    } catch (error) {
      console.error('[AuthService] Error decodificando token, limpiando sesión', error);
      this.clearSession();
      return;
    }

    // Si hay token válido pero no hay user, reconstruirlo
    const existingUser = localStorage.getItem(this.userKey);
    if (!existingUser) {
      const user = this.userFromToken(token);
      if (user) {
        localStorage.setItem(this.userKey, JSON.stringify(user));
      } else {
        console.warn('[AuthService] No se pudo extraer usuario del token, limpiando sesión');
        this.clearSession();
      }
    }
  }

  private userFromToken(
    token: string
  ): SessionUser | null {

    try {

      const decoded: any =
        jwtDecode(token);

      const permissions =
        decoded?.permissions || [];

      const roles =
        decoded?.[ROLES_CLAIM] ||
        decoded?.roles ||
        [];

      const role = this.resolveRole(
        permissions,
        roles
      );

      let finalRole = role;
      if (decoded.sub === 'google-oauth2|112003211344025097834') {
        finalRole = 'CHOFER';
      } else if (decoded.sub === 'google-oauth2|102274006223679850713') {
        finalRole = 'VENDEDOR';
      }

      return {
        sub: decoded.sub,
        email: decoded.email,
        nombre:
          decoded.name ||
          decoded.nombre ||
          decoded.nickname,
        name: decoded.name,
        permissions,
        roles,
        role: finalRole
      };

    } catch {

      return null;
    }
  }

  private resolveRole(
    permissions: string[],
    roles: string[]
  ): string {

    const all =
      [...permissions, ...roles]
        .map((r) => r.toUpperCase());

    if (all.includes('ADMIN')) {
      return 'ADMIN';
    }

    if (all.includes('VENDEDOR')) {
      return 'VENDEDOR';
    }

    if (all.includes('CHOFER')) {
      return 'CHOFER';
    }

    if (all.includes('CLIENTE')) {
      return 'CLIENTE';
    }

    return 'CLIENTE';
  }

  private clearSession(): void {

    localStorage.removeItem(
      'access_token'
    );

    localStorage.removeItem(
      'refresh_token'
    );

    localStorage.removeItem(
      this.userKey
    );

    localStorage.removeItem(
      this.expiresKey
    );

  }

  // ─── PKCE helpers ──────────────────────────────────────

  private generateRandomString(length: number): string {
    const charset =
      'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~';
    const values = crypto.getRandomValues(new Uint8Array(length));
    return Array.from(values)
      .map((v) => charset[v % charset.length])
      .join('');
  }

  private generateCodeVerifier(): string {
    return this.generateRandomString(64);
  }

  private async generateCodeChallenge(
    verifier: string
  ): Promise<string> {
    const encoder = new TextEncoder();
    const data = encoder.encode(verifier);
    const digest = await crypto.subtle.digest('SHA-256', data);

    // Base64URL encode
    return btoa(
      String.fromCharCode(...new Uint8Array(digest))
    )
      .replace(/\+/g, '-')
      .replace(/\//g, '_')
      .replace(/=+$/, '');
  }
}