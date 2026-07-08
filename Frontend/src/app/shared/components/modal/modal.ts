import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal.html',
  styleUrls: ['./modal.css']
})
export class ModalComponent {

  @Input()
  isOpen = false;

  @Input()
  title = 'Confirmación';

  @Input()
  message = '¿Deseas continuar?';

  @Input()
  confirmText = 'Confirmar';

  @Input()
  cancelText = 'Cancelar';

  @Input()
  type:
    | 'success'
    | 'warning'
    | 'danger'
    | 'info'
    = 'info';

  @Output()
  confirm =
    new EventEmitter<void>();

  @Output()
  close =
    new EventEmitter<void>();

  onConfirm(): void {
    this.confirm.emit();
  }

  onClose(): void {
    this.close.emit();
  }

  getIcon(): string {

    switch (this.type) {

      case 'success':
        return '✅';

      case 'warning':
        return '⚠️';

      case 'danger':
        return '🗑️';

      default:
        return 'ℹ️';
    }
  }
}