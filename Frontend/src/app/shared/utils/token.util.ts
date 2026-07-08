export class TokenUtil {

  private static readonly TOKEN_KEY = 'access_token';
  private static readonly REFRESH_TOKEN_KEY = 'refresh_token';

  // =========================
  // TOKEN
  // =========================
  static getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  static setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  static removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  // =========================
  // REFRESH TOKEN
  // =========================
  static getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  static setRefreshToken(token: string): void {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
  }

  static removeRefreshToken(): void {
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
  }

  // =========================
  // VALIDAR TOKEN
  // =========================
  static isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // =========================
  // EXTRAER PAYLOAD JWT
  // =========================
  static getPayload(): any {

    const token = this.getToken();

    if (!token) {
      return null;
    }

    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch (error) {
      console.error('Error al leer JWT', error);
      return null;
    }
  }

  // =========================
  // USER ID DESDE AUTH0 JWT
  // =========================
  static getUserId(): string | null {

    const payload = this.getPayload();

    return payload?.sub || null;
  }

  // =========================
  // ROLES / PERMISSIONS
  // =========================
  static getRoles(): string[] {

    const payload = this.getPayload();

    return (
      payload?.permissions ||
      payload?.roles ||
      []
    );
  }

  static hasRole(role: string): boolean {

    const roles = this.getRoles();

    return roles.includes(role);
  }

  // =========================
  // LOGOUT
  // =========================
  static clearSession(): void {
    this.removeToken();
    this.removeRefreshToken();
  }
}