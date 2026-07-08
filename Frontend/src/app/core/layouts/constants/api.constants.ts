export const API_CONSTANTS = {

  // ==========================
  // AUTH
  // ==========================
  AUTH: {
    LOGIN: '/auth/login',
    REFRESH: '/auth/refresh',
    VALIDATE: '/auth/validate',
    LOGOUT: '/auth/logout'
  },

  // ==========================
  // CATÁLOGO (PÚBLICO)
  // ==========================
  CATALOGO: {
    BASE: '/catalogo',
    PRODUCTOS: '/catalogo',
    ACTIVOS: '/catalogo/activos',
    DESTACADOS: '/catalogo/destacados',
    OFERTAS: '/catalogo/ofertas',
    NUEVOS: '/catalogo/nuevos',
    POR_CATEGORIA: '/catalogo/categoria',
    BUSCAR: '/catalogo/buscar'
  },

  // ==========================
  // PEDIDOS (JWT)
  // ==========================
  PEDIDOS: {
    BASE: '/pedidos',
    CHECKOUT: '/pedidos/checkout',
    USUARIO: '/pedidos/usuario',
    CANCELAR: '/pedidos/cancelar',
    ESTADO: '/pedidos/estado'
  },

  // ==========================
  // ENVÍOS (JWT)
  // ==========================
  ENVIOS: {
    BASE: '/envios',
    TRACKING: '/envios/tracking',
    USUARIO: '/envios/usuario',
    ENTREGAR: '/envios/entregar'
  },

  // ==========================
  // USUARIOS (JWT)
  // ==========================
  USUARIOS: {
    BASE: '/usuarios',
    PERFIL: '/usuarios/perfil',
    EXISTE: '/usuarios/existe',
    ACTIVAR: '/usuarios/activar',
    DESACTIVAR: '/usuarios/desactivar'
  },

  // ==========================
  // DASHBOARD ADMIN
  // ==========================
  DASHBOARD: {
    BASE: '/dashboard',
    ADMIN: '/dashboard/admin',
    USUARIO: '/dashboard/usuario',
    VENTAS: '/dashboard/ventas',
    LOGISTICA: '/dashboard/logistica'
  }
};