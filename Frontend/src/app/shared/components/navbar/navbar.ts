import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { CarritoService } from '../../../core/services/carrito.service';
import { LocationPicker } from '../location-picker/location-picker';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    FormsModule,
    LocationPicker
  ],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent implements OnInit {

  private authService = inject(AuthService);
  private carritoService = inject(CarritoService);
  private router = inject(Router);

  usuario: any = null;
  isLogged = false;
  isAdmin = false;
  cantidadCarrito = 0;

  // Nuevas propiedades
  searchTerm: string = '';
  isHelpMenuOpen: boolean = false;

  ngOnInit(): void {
    this.cargarUsuario();
    this.obtenerCantidadCarrito();
  }

  cargarUsuario(): void {
    this.isLogged = this.authService.isAuthenticated();
    if (this.isLogged) {
      this.usuario = this.authService.getUser();
      this.isAdmin = this.authService.isAdmin();
    }
  }

  obtenerCantidadCarrito(): void {
    this.carritoService.carrito$.subscribe(items => {
      this.cantidadCarrito = items.length;
    });
  }

  login(): void {
    this.authService.login().subscribe();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  // Métodos nuevos
  onSearch(): void {
    if (this.searchTerm.trim()) {
      this.router.navigate(['/catalogo'], { queryParams: { q: this.searchTerm.trim() } });
    }
  }

  toggleHelpMenu(): void {
    this.isHelpMenuOpen = !this.isHelpMenuOpen;
  }
}