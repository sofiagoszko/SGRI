export interface Requerimiento {
  archivosAdjuntos: string[];
  codigo: string;
  estado: string;
  prioridad: string;
  tipoRequerimiento: number;
  usuarioDestinatario: number;
  usuarioEmisor: number;
  fechaHora: string;
  asunto: string;
  categoriaTipo: string;
  comentarios: string[];
  descripcion: string;
}
