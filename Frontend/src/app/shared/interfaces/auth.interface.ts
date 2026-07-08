export interface AuthTokenResponse {
  accessToken: string;
  refreshToken?: string;
  expiresIn?: number;
  tokenType?: string;
  user?: SessionUser;
}

export interface SessionUser {
  sub: string;
  email?: string;
  nombre?: string;
  name?: string;
  role?: string;
  rol?: string;
  permissions?: string[];
  roles?: string[];
  auth0Id?: string;
  [key: string]: unknown;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials {
  nombre: string;
  email: string;
  password: string;
}
