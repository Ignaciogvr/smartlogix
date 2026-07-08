import {
  Component,
  Input
} from '@angular/core';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stats-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stats-card.html',
  styleUrls: ['./stats-card.css']
})
export class StatsCardComponent {

  @Input()
  titulo = '';

  @Input()
  valor: string | number = 0;

  @Input()
  icono = '📊';

  @Input()
  color = '#2e7d32';
}