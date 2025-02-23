import { FormEvent, useState } from "react";
import { Requerimiento } from "../types/Requerimiento";
import ColoresEstado from "../utils/ColoresEstado";
import axios from "axios";

interface ModalProps {
  requerimiento: Requerimiento | undefined;
  mostrarModal: boolean;
  closeModal: () => void;
}

const ModalDetalleRequerimiento = ({
  requerimiento,
  mostrarModal,
  closeModal,
}: ModalProps) => {
  const [nuevoComentario, setNuevoComentario] = useState("");
  const [asunto, setAsunto] = useState("");
  const [archivosAdjuntos, setArchivosAdjuntos] = useState([]);
  const [cargarComentario, setCargarComentario] = useState(false);
  const authToken = localStorage.getItem("authToken");
  const userId = localStorage.getItem("userId");
  if (requerimiento === undefined) return;

  const handleComentarioSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (nuevoComentario === "") return;

    const formData = new FormData();
    formData.append(
      "datos",
      new Blob(
        [
          JSON.stringify({
            asunto: asunto,
            descripcion: nuevoComentario,
            fecha_hora: new Date().toISOString().split(".")[0],
            usuarioEmisorComentario: userId,
          }),
        ],
        { type: "application/json" }
      )
    );

    if (archivosAdjuntos) {
      console.log(archivosAdjuntos);
      // archivosAdjuntos.map((file) => {
      //   formData.append(`archivos`, file);
      // });
    }

    axios
      .post(
        `${import.meta.env.VITE_API_URL}/requerimiento/${
          requerimiento.id
        }/comentario`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${authToken}`,
          },
        }
      )
      .then((res) => console.log(res));
  };

  return (
    <div
      className={`modal fade ${mostrarModal ? " show d-block" : ""}`}
      id="exampleModal"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div className="modal-dialog modal-xl">
        <div className="modal-content">
          <div className="modal-header">
            <h1 className="modal-title fs-5" id="exampleModalLabel">
              Requerimiento {requerimiento.codigo}
            </h1>
            <button
              type="button"
              className="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
              onClick={closeModal}
            ></button>
          </div>
          <div className="modal-body">
            <div className="row row-gap-5">
              <div className="col-6 border-end">
                <h1 className="mb-5">Datos del requerimiento</h1>
                <div className="row">
                  <div className="col-6 mb-3">
                    <p>
                      <b>Tipo</b>
                      <br />
                      {requerimiento.tipoRequerimiento.descripcion}
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Categoría</b>
                      <br />
                      {requerimiento.categoriaTipo}
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Prioridad</b>
                      <br />
                      <span
                        className={`${ColoresEstado[requerimiento.prioridad]}`}
                      >
                        {requerimiento.prioridad}
                      </span>
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Estado</b>
                      <br />
                      {requerimiento.estado}
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Usuario Emisor</b>
                      <br />
                      {`${requerimiento.usuarioEmisor.nombre} ${requerimiento.usuarioEmisor.apellido}`}
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Usuario Propietario</b>
                      <br />
                      {`${requerimiento.usuarioDestinatario.nombre} ${requerimiento.usuarioDestinatario.apellido}`}
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Descripción</b>
                      <br />
                      {requerimiento.descripcion}
                    </p>
                  </div>
                  <div className="col-6 mb-3">
                    <p>
                      <b>Asunto</b>
                      <br />
                      {requerimiento.asunto}
                    </p>
                    <p>
                      <b>Fecha y hora de emisión</b>
                      <br />
                      {new Date(requerimiento.fechaHora).toLocaleDateString()}
                    </p>
                  </div>
                </div>
              </div>
              <div className="col-6">
                <div className="border-bottom">
                  <h1 className="mb-3">Archivos adjuntos</h1>
                  <ul className="list-unstyled lista-botones">
                    {requerimiento.archivosAdjuntos.length ? (
                      requerimiento.archivosAdjuntos.map((file, index) => (
                        <li
                          className="bg-light rounded-pill px-4 fw-bold"
                          key={index}
                        >
                          {file}
                          <button className="btn">
                            <i className="bi bi-download"></i>
                          </button>
                        </li>
                      ))
                    ) : (
                      <p>No existen archivos adjuntos</p>
                    )}
                  </ul>
                </div>
                <div>
                  <div className="d-flex justify-content-between align-items-center my-3">
                    <h1>Comentarios</h1>
                    <button className="btn">
                      <i
                        className="bi bi-plus fs-3"
                        onClick={() => setCargarComentario(true)}
                      ></i>
                    </button>
                  </div>

                  <form
                    onSubmit={handleComentarioSubmit}
                    className={`${cargarComentario ? "" : "d-none"} px-2 mb-4`}
                  >
                    <div className="form-group mb-2">
                      <label htmlFor="asunto" className="small fw-semibold">
                        Asunto
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        id="asunto"
                        placeholder="Asunto"
                        required
                        onChange={(e) => setAsunto(e.target.value)}
                      />
                    </div>
                    <div className="form-group mb-3">
                      <label htmlFor="asunto" className="small fw-semibold">
                        Archivos adjuntos
                      </label>
                      <input
                        type="file"
                        className="form-control"
                        id="archivos"
                        multiple
                        onChange={(e) => setArchivosAdjuntos(e.target.value)}
                      />
                    </div>
                    <div className="d-flex gap-2">
                      <div className="form-group flex-grow-1">
                        <div className="form-floating">
                          <textarea
                            className="form-control textarea-sm"
                            placeholder="Leave a comment here"
                            id="comentario"
                            rows={1}
                            onChange={(e) => setNuevoComentario(e.target.value)}
                          ></textarea>
                          <label htmlFor="comentario">Comentario</label>
                        </div>
                      </div>
                      <button
                        type="submit"
                        className="btn btn-login-success text-white px-3"
                      >
                        Enviar
                      </button>
                    </div>
                  </form>

                  <ul className="list-unstyled lista-botones">
                    {requerimiento.comentarios &&
                    requerimiento.comentarios.length ? (
                      requerimiento.comentarios.map((comentario) => (
                        <li className="bg-light rounded-pill px-4 fw-bold">
                          {`${comentario.id} - ${new Date(
                            comentario.fecha_hora
                          ).toLocaleDateString()} ${comentario.asunto}`}
                          <button className="btn">
                            <i className="bi bi-eye"></i>
                          </button>
                        </li>
                      ))
                    ) : (
                      <p>No hay comentarios para este requerimiento</p>
                    )}
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <div className="modal-footer d-flex justify-content-center align-items-center my-2">
            <button
              onClick={closeModal}
              className="btn btn-login-success text-white px-3"
            >
              Aceptar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ModalDetalleRequerimiento;
