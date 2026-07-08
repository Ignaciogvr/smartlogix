import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { vendedorGuard } from './core/guards/vendedor.guard';
import { choferGuard } from './core/guards/chofer.guard';

export const routes: Routes = [

  // AUTH
  {
    path: 'auth/callback',
    loadComponent: () =>
      import('./features/auth/callback/callback')
        .then(m => m.CallbackComponent)
  },
  {
    path: 'unauthorized',
    loadComponent: () =>
      import('./features/auth/unauthorized/unauthorized')
        .then(m => m.UnauthorizedComponent) // ← ARREGLADO
  },

  // CLIENT
  {
    path: '',
    loadComponent: () =>
      import('./core/layouts/client-layout/client-layout')
        .then(m => m.ClientLayoutComponent),

    children: [
      {
        path: '',
        loadComponent: () =>
          import('./features/client/home/home')
            .then(m => m.HomeComponent)
      },
      {
        path: 'catalogo',
        loadComponent: () =>
          import('./features/client/catalogo/catalogo')
            .then(m => m.CatalogoComponent)
      },
      {
        path: 'producto/:id',
        loadComponent: () =>
          import('./features/client/producto/producto')
            .then(m => m.ProductoComponent)
      },
      {
        path: 'comparador',
        loadComponent: () =>
          import('./features/client/comparador/comparador')
            .then(m => m.ComparadorComponent)
      },
      {
        path: 'carrito',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/client/carrito/carrito')
            .then(m => m.CarritoComponent)
      },
      {
        path: 'checkout',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/client/checkout/checkout')
            .then(m => m.CheckoutComponent)
      },
      {
        path: 'pedidos',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/client/pedidos/pedidos')
            .then(m => m.PedidosComponent)
      },
      {
        path: 'tracking',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/client/tracking/tracking')
            .then(m => m.TrackingComponent)
      },
      {
        path: 'perfil',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/client/perfil/perfil')
            .then(m => m.PerfilComponent)
      },
      {
        path: 'ayuda',
        loadComponent: () =>
          import('./features/client/ayuda/ayuda-layout/ayuda-layout')
            .then(m => m.AyudaLayout),
        children: [
          {
            path: 'centro',
            loadComponent: () =>
              import('./features/client/ayuda/centro-ayuda/centro-ayuda')
                .then(m => m.CentroAyuda)
          },
          {
            path: 'devoluciones',
            loadComponent: () =>
              import('./features/client/ayuda/devoluciones/devoluciones')
                .then(m => m.Devoluciones)
          },
          {
            path: 'horarios',
            loadComponent: () =>
              import('./features/client/ayuda/horarios/horarios')
                .then(m => m.Horarios)
          },
          { path: '', redirectTo: 'centro', pathMatch: 'full' }
        ]
      }
    ]
  },

  // ADMIN
  {
    path: 'admin',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./core/layouts/admin-layout/admin-layout')
        .then(m => m.AdminLayoutComponent),

    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/admin/dashboard/dashboard')
            .then(m => m.DashboardComponent)
      },
      {
        path: 'productos',
        loadComponent: () =>
          import('./features/admin/productos/productos')
            .then(m => m.ProductosComponent)
      },
      {
        path: 'inventario',
        loadComponent: () =>
          import('./features/admin/inventario/inventario')
            .then(m => m.InventarioComponent)
      },
      {
        path: 'pedidos',
        loadComponent: () =>
          import('./features/admin/pedidos/pedidos-admin')
            .then(m => m.PedidosAdminComponent)
      },
      {
        path: 'usuarios',
        loadComponent: () =>
          import('./features/admin/usuarios/usuarios')
            .then(m => m.UsuariosComponent)
      },
      {
        path: 'envios',
        loadComponent: () =>
          import('./features/admin/envios/envios')
            .then(m => m.EnviosComponent)
      }
    ]
  },

  // VENDEDOR
  {
    path: 'vendedor',
    canActivate: [vendedorGuard],
    loadComponent: () =>
      import('./core/layouts/vendedor-layout/vendedor-layout')
        .then(m => m.VendedorLayoutComponent),

    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/vendedor/dashboard/dashboard')
            .then(m => m.DashboardVendedorComponent)
      },
      {
        path: 'productos',
        loadComponent: () =>
          import('./features/vendedor/productos/productos')
            .then(m => m.ProductosVendedorComponent)
      },
      {
        path: 'pedidos',
        loadComponent: () =>
          import('./features/vendedor/pedidos/pedidos')
            .then(m => m.PedidosVendedorComponent)
      },
      {
        path: 'envios',
        loadComponent: () =>
          import('./features/vendedor/envios/envios')
            .then(m => m.EnviosVendedorComponent)
      },
      {
        path: 'perfil',
        loadComponent: () =>
          import('./features/vendedor/perfil/perfil')
            .then(m => m.PerfilVendedorComponent)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // CHOFER
  {
    path: 'chofer',
    canActivate: [choferGuard],
    loadComponent: () =>
      import('./core/layouts/chofer-layout/chofer-layout')
        .then(m => m.ChoferLayoutComponent),

    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/chofer/dashboard/dashboard')
            .then(m => m.DashboardChoferComponent)
      },
      {
        path: 'envios',
        loadComponent: () =>
          import('./features/chofer/envios/envios')
            .then(m => m.EnviosChoferComponent)
      },
      {
        path: 'perfil',
        loadComponent: () =>
          import('./features/chofer/perfil/perfil')
            .then(m => m.PerfilChoferComponent)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  { path: '**', redirectTo: '' }
];