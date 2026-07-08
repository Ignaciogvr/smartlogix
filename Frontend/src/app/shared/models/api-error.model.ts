/**
 * Modelo unificado de error HTTP para toda la aplicación.
 * Permite diferenciar entre errores reales y respuestas vacías válidas.
 */
export interface ApiError {
  status: number;
  message: string;
  type: ApiErrorType;
  originalError?: any;
}

export enum ApiErrorType {
  NETWORK = 'NETWORK',           // status 0 - Sin conexión
  UNAUTHORIZED = 'UNAUTHORIZED', // status 401 - No autenticado
  FORBIDDEN = 'FORBIDDEN',       // status 403 - Sin permisos
  NOT_FOUND = 'NOT_FOUND',       // status 404 - Recurso no encontrado
  TIMEOUT = 'TIMEOUT',           // TimeoutError
  SERVER_ERROR = 'SERVER_ERROR', // status 500+ - Error interno
  CLIENT_ERROR = 'CLIENT_ERROR', // status 400-499 (excepto 401, 403, 404)
  UNKNOWN = 'UNKNOWN'            // Otro tipo de error
}

/**
 * Determina el tipo de error basándose en el código HTTP.
 */
export function getErrorType(error: any): ApiErrorType {
  if (error?.name === 'TimeoutError') {
    return ApiErrorType.TIMEOUT;
  }
  
  const status = error?.status ?? 0;
  
  if (status === 0) return ApiErrorType.NETWORK;
  if (status === 401) return ApiErrorType.UNAUTHORIZED;
  if (status === 403) return ApiErrorType.FORBIDDEN;
  if (status === 404) return ApiErrorType.NOT_FOUND;
  if (status >= 500) return ApiErrorType.SERVER_ERROR;
  if (status >= 400 && status < 500) return ApiErrorType.CLIENT_ERROR;
  
  return ApiErrorType.UNKNOWN;
}

/**
 * Genera un mensaje de error apropiado para mostrar al usuario.
 */
export function getErrorMessage(error: any, context?: string): string {
  const type = getErrorType(error);
  const contextStr = context ? ` ${context}` : '';
  
  switch (type) {
    case ApiErrorType.NETWORK:
      return `No hay conexión con el servidor${contextStr}. Verifica tu conexión a internet.`;
    
    case ApiErrorType.UNAUTHORIZED:
      return 'Tu sesión ha expirado. Por favor, inicia sesión nuevamente.';
    
    case ApiErrorType.FORBIDDEN:
      return `No tienes permisos para acceder a${contextStr}.`;
    
    case ApiErrorType.NOT_FOUND:
      return `El recurso solicitado${contextStr} no fue encontrado.`;
    
    case ApiErrorType.TIMEOUT:
      return `La solicitud${contextStr} tardó demasiado tiempo. Intenta nuevamente.`;
    
    case ApiErrorType.SERVER_ERROR:
      return `Error interno del servidor${contextStr}. Por favor, contacta al soporte técnico.`;
    
    case ApiErrorType.CLIENT_ERROR:
      return error?.error?.message || 
             error?.message || 
             `Error en la solicitud${contextStr}. Verifica los datos ingresados.`;
    
    default:
      return `Error inesperado${contextStr}. Por favor, intenta nuevamente.`;
  }
}

/**
 * Verifica si un error HTTP indica que debería redirigir al login.
 */
export function shouldRedirectToLogin(error: any): boolean {
  return getErrorType(error) === ApiErrorType.UNAUTHORIZED;
}

/**
 * Verifica si una respuesta exitosa contiene datos vacíos.
 */
export function isEmptyResponse(data: any): boolean {
  if (data === null || data === undefined) return true;
  if (Array.isArray(data) && data.length === 0) return true;
  if (typeof data === 'object' && Object.keys(data).length === 0) return true;
  return false;
}
