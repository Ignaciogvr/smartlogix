import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar';
import { NavbarComponent } from '../../../shared/components/navbar/navbar';

@Component({
  selector: 'app-vendedor-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    SidebarComponent,
    NavbarComponent
  ],
  templateUrl: './vendedor-layout.html',
  styleUrls: ['./vendedor-layout.css']
})
export class VendedorLayoutComponent {}
