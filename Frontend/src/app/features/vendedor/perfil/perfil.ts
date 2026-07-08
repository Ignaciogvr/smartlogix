import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-perfil-vendedor',
  standalone: true,
  imports: [CommonModule, CardModule],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css']
})
export class PerfilVendedorComponent implements OnInit {
  user: any;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.user = this.authService.getUser();
  }
}
