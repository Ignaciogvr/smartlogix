export const ROLES = {

  ADMIN: 'ROLE_ADMIN',

  CLIENTE: 'ROLE_CLIENTE',

  VENDEDOR: 'ROLE_VENDEDOR',

  LOGISTICA: 'ROLE_LOGISTICA'

};

// ==========================
// ARRAY DE ROLES
// ==========================

export const ADMIN_ROLES = [
  ROLES.ADMIN
];

export const DASHBOARD_ROLES = [
  ROLES.ADMIN,
  ROLES.VENDEDOR,
  ROLES.LOGISTICA
];

export const USER_ROLES = [
  ROLES.CLIENTE,
  ROLES.ADMIN,
  ROLES.VENDEDOR
];