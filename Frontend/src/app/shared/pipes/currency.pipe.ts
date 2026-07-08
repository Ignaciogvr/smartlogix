import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'currencyClp',
  standalone: true
})
export class CurrencyClpPipe implements PipeTransform {

  transform(value: number | string | null | undefined): string {

    if (value === null || value === undefined) return '$0';

    const amount = Number(value);

    if (isNaN(amount)) return '$0';

    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0
    }).format(amount);
  }
}