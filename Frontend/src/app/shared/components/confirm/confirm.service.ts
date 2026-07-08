import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface ConfirmData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  isDanger?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ConfirmService {
  private confirmStateSubject = new Subject<ConfirmData | null>();
  public confirmState$ = this.confirmStateSubject.asObservable();
  
  private currentResolver: ((value: boolean) => void) | null = null;

  constructor() { }

  ask(data: ConfirmData): Promise<boolean> {
    this.confirmStateSubject.next(data);
    return new Promise((resolve) => {
      this.currentResolver = resolve;
    });
  }

  confirm() {
    if (this.currentResolver) {
      this.currentResolver(true);
      this.close();
    }
  }

  cancel() {
    if (this.currentResolver) {
      this.currentResolver(false);
      this.close();
    }
  }

  private close() {
    this.currentResolver = null;
    this.confirmStateSubject.next(null);
  }
}
