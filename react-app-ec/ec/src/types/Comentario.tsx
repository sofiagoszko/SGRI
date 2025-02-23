import { Usuario } from "./Usuario";

export interface Comentario {
  id: number;
  asunto: string;
  descripcion: string;
  fecha_hora: string;
  usuarioEmisorComentario: Usuario;
  requerimiento: number;
  archivosComentario: string[];
}
