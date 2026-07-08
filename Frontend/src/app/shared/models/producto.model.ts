export interface Producto {

  id: number;

  nombre: string;

  descripcion: string;

  precio: number;

  stock: number;

  categoria: string;

  imagenUrl: string;

  imagenes?: string[];

  activo: boolean;

  destacado?: boolean;

  oferta?: boolean;

  nuevo?: boolean;
  descripcionCorta?: string;
  precioAnterior?: number;
  descuentoPorcentaje?: number;
  marca?: string;
  modelo?: string;
  fabricante?: string;
  sku?: string;
  garantia?: string;
  peso?: string;
  dimensiones?: string;
  material?: string;
  color?: string;
  paisFabricacion?: string;
  rating?: number;
  vendidos?: number;
  totalRatings?: number;
}