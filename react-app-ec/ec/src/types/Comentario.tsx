export interface Comentario {
  id: number;
  asunto: string;
  descripcion: string;
  fecha_hora: string;
  usuarioEmisorComentario: number;
  requerimiento: number;
  archivosComentario: string[];
}
