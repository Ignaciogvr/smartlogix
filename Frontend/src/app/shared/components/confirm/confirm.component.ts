import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmService } from './confirm.service';

@Component({
  selector: 'app-confirm',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal-overlay" *ngIf="confirmService.confirmState$ | async as state" (click)="confirmService.cancel()">
      <div class="modal-content animate-fade-in" style="max-width: 400px; padding: 24px; text-align: center;" (click)="$event.stopPropagation()">
        
        <div class="mb-4" style="font-size: 3rem;">
          <span *ngIf="state.isDanger">⚠️</span>
          <span *ngIf="!state.isDanger">❓</span>
        </div>
        
        <h2 class="mb-2" style="font-size: 1.5rem;">{{ state.title }}</h2>
        <p class="text-muted mb-6">{{ state.message }}</p>
        
        <div class="flex gap-2 justify-center">
          <button class="btn btn-secondary" style="flex: 1" (click)="confirmService.cancel()">
            {{ state.cancelText || 'Cancelar' }}
          </button>
          <button class="btn" style="flex: 1" 
                  [ngClass]="state.isDanger ? 'btn-danger' : 'btn-primary'"
                  (click)="confirmService.confirm()">
            {{ state.confirmText || 'Confirmar' }}
          </button>
        </div>
        
      </div>
    </div>
  `,
  styles: [`
    .modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: rgba(0,0,0,0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 10000;
      backdrop-filter: blur(2px);
    }
    .modal-content {
      background: var(--bg-card);
      border-radius: var(--radius-lg);
      box-shadow: var(--shadow-lg);
      width: 90%;
    }
  `]
})
export class ConfirmComponent {
  confirmService = inject(ConfirmService);
}
