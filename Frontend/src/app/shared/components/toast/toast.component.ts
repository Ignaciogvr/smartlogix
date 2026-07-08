import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService, Toast } from './toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      <div *ngFor="let toast of toastService.toasts$ | async" 
           class="toast-item"
           [ngClass]="'toast-' + toast.type"
           (click)="toastService.remove(toast.id)">
        <span class="toast-icon">
          <ng-container *ngIf="toast.type === 'success'">✅</ng-container>
          <ng-container *ngIf="toast.type === 'error'">❌</ng-container>
          <ng-container *ngIf="toast.type === 'warning'">⚠️</ng-container>
          <ng-container *ngIf="toast.type === 'info'">ℹ️</ng-container>
        </span>
        <span class="toast-message">{{ toast.message }}</span>
      </div>
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      bottom: 20px;
      right: 20px;
      z-index: 9999;
      display: flex;
      flex-direction: column;
      gap: 10px;
    }
    
    .toast-item {
      display: flex;
      align-items: center;
      gap: 12px;
      min-width: 300px;
      padding: 16px 20px;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      cursor: pointer;
      animation: slideIn 0.3s ease-out forwards;
      background: #fff;
      color: #333;
      border-left: 5px solid #ccc;
    }

    .toast-item:hover {
      box-shadow: 0 6px 16px rgba(0,0,0,0.2);
    }

    .toast-success { border-left-color: var(--success); }
    .toast-error { border-left-color: var(--danger); }
    .toast-warning { border-left-color: var(--warning); }
    .toast-info { border-left-color: var(--info); }

    .toast-message { font-weight: 500; }
    
    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `]
})
export class ToastComponent {
  toastService = inject(ToastService);
}
