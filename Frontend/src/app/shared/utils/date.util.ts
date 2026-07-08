export class DateUtil {

  // =========================
  // FORMATEAR FECHA
  // =========================
  static format(
    date: string | Date | null | undefined
  ): string {

    if (!date) {
      return '';
    }

    const fecha = new Date(date);

    return fecha.toLocaleDateString('es-CL', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  }

  // =========================
  // FORMATEAR FECHA + HORA
  // =========================
  static formatDateTime(
    date: string | Date | null | undefined
  ): string {

    if (!date) {
      return '';
    }

    const fecha = new Date(date);

    return fecha.toLocaleString('es-CL', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // =========================
  // FECHA ACTUAL
  // =========================
  static now(): Date {
    return new Date();
  }

  // =========================
  // DIFERENCIA EN DÍAS
  // =========================
  static differenceInDays(
    startDate: string | Date,
    endDate: string | Date
  ): number {

    const inicio = new Date(startDate).getTime();
    const fin = new Date(endDate).getTime();

    const diff = fin - inicio;

    return Math.floor(diff / (1000 * 60 * 60 * 24));
  }
}