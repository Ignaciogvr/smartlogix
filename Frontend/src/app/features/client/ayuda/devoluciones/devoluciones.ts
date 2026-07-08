import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-devoluciones',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './devoluciones.html',
  styleUrls: ['./devoluciones.css']
})
export class Devoluciones {
  feedbackDado = false;

  darFeedback(util: boolean) {
    this.feedbackDado = true;
    console.log('Feedback de devoluciones util:', util);
  }
}
