export interface Comentario {
  asunto: string;
  descripcion: string;
  fecha_hora: string;
  usuarioEmisorComentario: number;
  requerimiento: number;
  archivosComentario: string[];
}
