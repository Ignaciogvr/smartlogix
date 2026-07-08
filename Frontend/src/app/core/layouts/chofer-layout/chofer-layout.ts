import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-chofer-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './chofer-layout.html',
  styleUrls: ['./chofer-layout.css']
})
export class ChoferLayoutComponent {
  auth = inject(AuthService);
  router = inject(Router);

  nombreUsuario = this.auth.getUser()?.nombre || this.auth.getUser()?.name || 'Chofer';

  menuItems = [
    { label: 'Dashboard', route: '/chofer/dashboard', icon: '📊' },
    { label: 'Mis Envíos', route: '/chofer/envios', icon: '📦' },
    { label: 'Perfil', route: '/chofer/perfil', icon: '👤' }
  ];

  logout(): void {
    this.auth.logout();
  }
}
