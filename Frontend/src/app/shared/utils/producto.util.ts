import { Producto } from '../models/producto.model';

/**
 * Normaliza el objeto crudo que viene del backend.
 * Extrae de forma segura la lista de imágenes reales que vienen desde la base de datos (CDN).
 */
export function normalizarProducto(raw: any): Producto {
  const nombre = raw?.nombre ?? 'Producto';

  // Obtener imágenes exclusivamente desde el array proporcionado por el backend
  const imagenes: string[] = Array.isArray(raw?.imagenes) ? raw.imagenes : [];
  
  // Debug: log para verificar imágenes recibidas
  if (imagenes.length > 0) {
    console.log(`[normalizarProducto] ${nombre}: imagenes[0] = ${imagenes[0]}`);
  }

  const imagenUrl = raw?.imagenPrincipal || (imagenes.length > 0 ? imagenes[0] : '');

  const destacado = Boolean(raw?.destacado);
  const oferta = Boolean(raw?.oferta);
  const nuevo = Boolean(raw?.nuevo);

  return {
    id: Number(raw?.id ?? 0),
    nombre,
    descripcion: raw?.descripcion ?? '',
    precio: Number(raw?.precio ?? 0),
    stock: Number(raw?.stock ?? 0),
    categoria: raw?.categoria ?? 'General',
    imagenUrl,
    imagenes,
    activo: raw?.activo ?? (raw?.estado === 'ACTIVO'),
    destacado,
    oferta,
    nuevo,
    descripcionCorta: raw?.descripcionCorta,
    precioAnterior: raw?.precioAnterior != null ? Number(raw.precioAnterior) : undefined,
    descuentoPorcentaje: raw?.descuentoPorcentaje != null ? Number(raw.descuentoPorcentaje) : undefined,
    marca: raw?.marca,
    modelo: raw?.modelo,
    fabricante: raw?.fabricante,
    sku: raw?.sku,
    garantia: raw?.garantia,
    peso: raw?.peso,
    dimensiones: raw?.dimensiones,
    material: raw?.material,
    color: raw?.color,
    paisFabricacion: raw?.paisFabricacion,
    rating: raw?.rating != null ? Number(raw.rating) : undefined,
    vendidos: raw?.vendidos != null ? Number(raw.vendidos) : undefined,
    totalRatings: raw?.totalRatings != null ? Number(raw.totalRatings) : undefined
  };
}

export function normalizarProductos(lista: any[]): Producto[] {
  return (lista ?? []).map(normalizarProducto);
}

/**
 * Retorna la imagen principal del producto.
 * Valida que sea una URL o ruta válida.
 */
export function imagenProducto(producto: Producto | any): string {
  if (!producto) return imagenAlternativa();
  
  // Validar imagenUrl
  if (producto.imagenUrl && typeof producto.imagenUrl === 'string' && producto.imagenUrl.trim()) {
    return producto.imagenUrl;
  }
  
  // Validar array de imágenes
  if (Array.isArray(producto.imagenes) && producto.imagenes.length > 0) {
    const primeraImagen = producto.imagenes[0];
    if (typeof primeraImagen === 'string' && primeraImagen.trim()) {
      return primeraImagen;
    }
  }
  
  // Retornar placeholder si nada funciona
  return imagenAlternativa();
}

/**
 * Imagen alternativa estricta en caso de error de carga.
 */
export function imagenAlternativa(): string {
  return 'assets/images/product-placeholder.webp';
}
