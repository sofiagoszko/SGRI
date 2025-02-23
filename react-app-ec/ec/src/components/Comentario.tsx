import { useState } from "react";
import { Comentario as ComentarioInterface } from "../types/Comentario";
import { Download } from "lucide-react";

interface ComentarioProps {
  comentario: ComentarioInterface;
}

const Comentario = ({ comentario }: ComentarioProps) => {
  const [openComentario, setOpenComentario] = useState(false);

  return (
    <li
      className="bg-light rounded-3 px-4 fw-normal d-flex flex-column py-3"
      key={comentario.id}
    >
      <div className="d-flex items-center justify-content-between w-100">
        <p className="mb-0 fw-semibold">
          {`${comentario.usuarioEmisorComentario.userName} - ${new Date(
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
          {comentario.archivosComentario.length ? (
            <ul className="list-unstyled">
              {comentario.archivosComentario.map((file) => (
                <li>
                  <a
                    href={file}
                    target="_blank"
                    className="text-decoration-none"
                  >
                    <Download
                      className="me-2"
                      style={{ width: "16px", height: "16px" }}
                    />
                    {file.split("/").slice(-1)[0].split("_").slice(1).join("_")}
                  </a>
                </li>
              ))}
            </ul>
          ) : (
            <></>
          )}
        </>
      )}
    </li>
  );
};
export default Comentario;
