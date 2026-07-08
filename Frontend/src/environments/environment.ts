export const environment = {
  production: false,

  api: {
    bff: 'http://localhost:8080'
  },

  auth: {
    domain: 'dev-nomnv0fhn3zpzt4t.us.auth0.com',
    clientId: 't52W67XCqLRhDFVhqIiIcY4GFVRi491f',
    audience: 'https://smartlogix-api',
    redirectUri: (typeof window !== 'undefined' ? window.location.origin : 'http://localhost:4200') + '/auth/callback'
  }
};