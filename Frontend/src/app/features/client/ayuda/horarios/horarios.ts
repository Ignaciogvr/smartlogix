import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-horarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './horarios.html',
  styleUrls: ['./horarios.css']
})
export class Horarios {
  feedbackDado = false;

  darFeedback(util: boolean) {
    this.feedbackDado = true;
    console.log('Feedback de horarios util:', util);
  }
}
