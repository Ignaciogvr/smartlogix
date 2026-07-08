import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Region {
  id: number;
  nombre: string;
  comunas: string[];
}

@Component({
  selector: 'app-location-picker',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './location-picker.html',
  styleUrl: './location-picker.css',
})
export class LocationPicker implements OnInit {
  isOpen = false;
  
  regionSeleccionada: string = '';
  comunaSeleccionada: string = '';
  
  ubicacionGuardada: { region: string, comuna: string } | null = null;

  regiones: Region[] = [
    {
      id: 1,
      nombre: 'Región Metropolitana',
      comunas: ['Santiago', 'Providencia', 'Las Condes', 'Ñuñoa', 'Maipú']
    },
    {
      id: 2,
      nombre: 'Región de Valparaíso',
      comunas: ['Valparaíso', 'Viña del Mar', 'Quilpué', 'Villa Alemana']
    },
    {
      id: 3,
      nombre: 'Región del Biobío',
      comunas: ['Concepción', 'Talcahuano', 'Chiguayante', 'San Pedro de la Paz']
    }
  ];

  comunasDisponibles: string[] = [];

  ngOnInit() {
    this.cargarUbicacion();
  }

  toggleDropdown() {
    this.isOpen = !this.isOpen;
  }
  
  closeDropdown() {
    this.isOpen = false;
  }

  onRegionChange() {
    this.comunaSeleccionada = '';
    const region = this.regiones.find(r => r.nombre === this.regionSeleccionada);
    this.comunasDisponibles = region ? region.comunas : [];
  }

  guardarUbicacion() {
    if (this.regionSeleccionada && this.comunaSeleccionada) {
      this.ubicacionGuardada = {
        region: this.regionSeleccionada,
        comuna: this.comunaSeleccionada
      };
      
      if (typeof window !== 'undefined') {
        localStorage.setItem('user_location', JSON.stringify(this.ubicacionGuardada));
      }
      
      this.closeDropdown();
    }
  }

  cargarUbicacion() {
    if (typeof window !== 'undefined') {
      const guardado = localStorage.getItem('user_location');
      if (guardado) {
        try {
          this.ubicacionGuardada = JSON.parse(guardado);
          this.regionSeleccionada = this.ubicacionGuardada?.region || '';
          this.onRegionChange();
          this.comunaSeleccionada = this.ubicacionGuardada?.comuna || '';
        } catch (e) {
          console.error('Error al cargar ubicación', e);
        }
      }
    }
  }
}
