import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from './shared/components/toast/toast.component';
import { ConfirmComponent } from './shared/components/confirm/confirm.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent, ConfirmComponent],
  template: `
    <router-outlet></router-outlet>
    <app-toast></app-toast>
    <app-confirm></app-confirm>
  `
})
export class AppComponent {


}