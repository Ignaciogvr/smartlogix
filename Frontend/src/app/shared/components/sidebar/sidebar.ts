import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.css']
})
export class SidebarComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  user = this.authService.getUser();

  isAdmin = this.authService.isAdmin();
  isLogistica = this.authService.hasRole('LOGISTICA');
  isVendedor = this.authService.hasRole('VENDEDOR');

  logout(): void {

    this.authService.logout();

    this.router.navigate(['/login']);
  }
}