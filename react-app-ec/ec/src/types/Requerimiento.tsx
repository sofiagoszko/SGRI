import { Usuario } from "./Usuario";
import { TipoRequerimiento } from "./TipoRequerimiento";
import { Comentario } from "./Comentario";

export interface Requerimiento {
  id: number;
  archivosAdjuntos: string[];
  codigo: string;
  estado: string;
  prioridad: string;
  tipoRequerimiento: TipoRequerimiento;
  usuarioDestinatario: Usuario;
  usuarioEmisor: Usuario;
  fechaHora: string;
  asunto: string;
  categoriaTipo: string;
  comentarios: Comentario[];
  descripcion: string;
}
