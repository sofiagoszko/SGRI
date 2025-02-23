import { Categoria } from "./Categoria";

export interface TipoRequerimiento {
  codigo: string;
  descripcion: string;
  categorias: Categoria[];
}
