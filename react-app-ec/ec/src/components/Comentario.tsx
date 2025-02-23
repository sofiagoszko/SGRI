import { useState } from "react";
import { Comentario as ComentarioInterface } from "../types/Comentario";

interface ComentarioProps {
  comentario: ComentarioInterface;
}

const Comentario = ({ comentario }: ComentarioProps) => {
  const [openComentario, setOpenComentario] = useState(false);

  return (
    <li
      className="bg-light rounded-3 px-4 fw-semibold d-flex flex-column py-3"
      key={comentario.id}
    >
      <div className="d-flex items-center justify-content-between w-100">
        <p className="mb-0">
          {`${comentario.id} - ${new Date(
            comentario.fecha_hora
          ).toLocaleDateString()} ${comentario.asunto}`}
        </p>
        <button
          className="btn"
          onClick={() => setOpenComentario(!openComentario)}
        >
          <i className={`bi ${openComentario ? "bi-eye-slash" : "bi-eye"}`}></i>
        </button>
      </div>
      {openComentario && (
        <>
          <p className="fw-normal small">{comentario.descripcion}</p>
        </>
      )}
    </li>
  );
};
export default Comentario;
