import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  templateUrl: './unauthorized.html'
})
export class UnauthorizedComponent {

  constructor(private router: Router) {}

  volver() {
    this.router.navigate(['/']);
  }
}