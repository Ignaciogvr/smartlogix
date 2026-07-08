import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class BffService {
  private baseUrl = environment.api.bff;

  get url() {
    return this.baseUrl;
  }
}