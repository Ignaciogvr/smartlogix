import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BffService } from '../../../core/services/bff.service';

@Component({
  selector: 'app-comparador',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './comparador.html',
  styleUrls: ['./comparador.css']
})
export class ComparadorComponent implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private bff = inject(BffService);

  productos: any[] = [];
  loading = false;
  error = '';

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const ids = params['ids'];
      if (ids) {
        this.cargarComparacion(ids);
      }
    });
  }

  cargarComparacion(ids: string) {
    this.loading = true;
    this.http.get<any>(`${this.bff.url}/api/comparador?ids=${ids}`).subscribe({
      next: (res) => {
        this.productos = res.data || [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Error cargando productos para comparar.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  // Helper properties to iterate dynamically in template
  get caracteristicas() {
    return ['marca', 'modelo', 'color', 'peso', 'dimensiones'];
  }
}
