import React, { useState, useEffect } from "react";
import { useAuth } from "../../utils/AuthContext.jsx";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout";
import "./MisAsignaciones.css"; // Importa los estilos CSS
import { tablePaginationClasses } from "@mui/material";
import { ColoresEstado } from "../../utils/ColoresEstado";

const Nuevo = () => {
  const [mostrarModal, setMostrarModal] = useState(false);
  const manejadorFiltros = (tipo, valor) => {
    setFiltros({ ...filtros, [tipo]: valor });
  };
  const [filtros, setFiltros] = useState({
    categoriaTipo: "",
    estado: "",
    tipoRequerimiento: "",
    usuarioDestinatario: "",
  });
  const restablecerFiltros = () => {
    setFiltros({
      categoriaTipo: "",
      estado: "",
      tipoRequerimiento: "",
      usuarioDestinatario: "",
    });
  };
  const [requerimientos, setRequerimientos] = useState([]);

  // Fetch de los requerimientos desde la API
  useEffect(() => {
    const fetchRequerimientos = async () => {
      try {
        const response = await fetch("api/requerimiento/requerimientos");
        if (!response.ok) {
          throw new Error("Error al obtener los requerimientos");
        }
        const data = await response.json();
        setRequerimientos(data);
      } catch (error) {
        console.error("Error:", error);
      }
    };

    fetchRequerimientos();
  }, []);

  const manejadorFiltros = (tipo, valor) => {
    setFiltros({ ...filtros, [tipo]: valor });
  };

  const requerimientosFiltrados = requerimientos.filter((req) => {
    const filtroCategoria =
      filtros.categoriaTipo === "" ||
      req.categoriaTipo === filtros.categoriaTipo;
    const filtroEstado = filtros.estado === "" || req.estado === filtros.estado;
    const filtroTipo =
      filtros.tipoRequerimiento === "" ||
      req.tipoRequerimiento === filtros.tipoRequerimiento;
    const filtroUsuario =
      filtros.usuarioDestinatario === "" ||
      req.usuarioDestinatario === filtros.usuarioDestinatario;
    return filtroCategoria && filtroEstado && filtroTipo && filtroUsuario;
  });
  const showModal = () => {
    setMostrarModal(true);
  };
  const closeModal = () => {
    setMostrarModal(false);
  };
  return (
    <Layout>
      <section className="content-placeholder bg-white rounded-4 align-self-center flex-grow-1 mb-5 p-5">
        <h2>Mis Asignaciones</h2>

        {/* Filtros */}
        <div className="overflow-x-auto d-flex justify-content-evenly gap-3 mb-4">
          <div className="form-floating">
            <select
              name="tipos"
              id="tipos"
              className="form-select min-w-select"
              value={filtros.tipoRequerimiento}
              onChange={(e) =>
                manejadorFiltros("tipoRequerimiento", e.target.value)
              }
            >
              <option value=""></option>
              <option value="REH">REH</option>
              <option value="ERR">ERR</option>
              <option value="GOP">GOP</option>
            </select>
            <label htmlFor="tipos">Todos los tipos</label>
          </div>
          <div className="form-floating">
            <select
              name="categorias"
              id="categorias"
              className="form-select min-w-select"
              value={filtros.categoriaTipo}
              onChange={(e) =>
                manejadorFiltros("categoriaTipo", e.target.value)
              }
            >
              <option value=""></option>
              <option>Hardware</option>
              <option>Software</option>
              <option>Red</option>
              <option>Seguridad</option>
            </select>
            <label htmlFor="tipos">Todos las categorías</label>
          </div>
          <div className="form-floating">
            <select
              name="estados"
              id="estados"
              className="form-select min-w-select"
              value={filtros.estado}
              onChange={(e) => manejadorFiltros("estado", e.target.value)}
            >
              <option value=""></option>
              <option>Abierto</option>
              <option>Asignado</option>
            </select>
            <label htmlFor="tipos">Todos los estados</label>
          </div>

          <div className="form-floating">
            <select
              name="Propietario"
              id="Propietario"
              className="form-select min-w-select"
              value={filtros.usuarioDestinatario}
              onChange={(e) =>
                manejadorFiltros("usuarioDestinatario", e.target.value)
              }
            >
              <option value=""></option>
              <option>Sofia</option>
              <option>Aldo</option>
              <option>Seba</option>
              <option>Silvia</option>
            </select>
            <label htmlFor="tipos">Todos los Propietarios</label>
          </div>
          <div className="d-flex justify-content-center">
            {" "}
            {/* Centrar el botón */}
            <button className="btn btn-secondary" onClick={restablecerFiltros}>
              Desfiltrar
            </button>
          </div>
        </div>
        {/* FIN Filtros */}
        {/* Tabla */}
        <div className="table-responsive">
          <table className="table">
            <thead className="table-primary-color">
              <tr>
                <th scope="col">Código</th>
                <th scope="col">Estado</th>
                <th scope="col">Prioridad</th>
                <th scope="col">Tipo</th>
                <th scope="col">Propietario</th>
                <th scope="col">Fecha de Alta</th>
                <th scope="col">Asunto</th>
                <th scope="col">Categoría</th>
              </tr>
            </thead>
            <tbody>
              {requerimientosFiltrados.map((req) => (
                <tr>
                  <td scope="col">
                    <div className="d-flex gap-2 align-items-center">
                      {req.codigo}
                      <button className="btn" onClick={showModal}>
                        <i className="bi bi-eye"></i>
                      </button>
                    </div>
                  </td>
                  <td scope="col" className="align-middle">
                    {req.estado}
                  </td>
                  <td scope="col" className="align-middle">
                    <span className={ColoresEstado[req.prioridad!]}>
                      {req.prioridad}
                    </span>
                  </td>
                  <td scope="col" className="align-middle">
                    {req.tipoRequerimiento}
                  </td>
                  <td scope="col" className="align-middle">
                    {req.usuarioDestinatario}
                  </td>
                  <td scope="col" className="align-middle">
                    {req.fechaHora}
                  </td>
                  <td scope="col" className="align-middle">
                    {req.asunto}
                  </td>
                  <td scope="col" className="align-middle">
                    {req.categoriaTipo}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {/* Fin Tabla */}
      </section>

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
                Requerimiento REH-2024-0000000012
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
                        REH
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Categoría</b>
                        <br />
                        SOFTWARE
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Prioridad</b>
                        <br />
                        <span className="text-danger">URGENTE</span>
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Estado</b>
                        <br />
                        ASIGNADO
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Usuario Emisor</b>
                        <br />
                        SILVIA ROMERO
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Usuario Propietario</b>
                        <br />
                        ADMIN
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Descripción</b>
                        <br />
                        Cuando quiero editar los requerimientos me figura un
                        error de actualización, pero no me toma el código de la
                        licencia.
                      </p>
                    </div>
                    <div className="col-6 mb-3">
                      <p>
                        <b>Asunto</b>
                        <br />
                        ACTUALIZACIÓN
                      </p>
                      <p>
                        <b>Fecha y hora de emisión</b>
                        <br />
                        02/11/2024
                      </p>
                    </div>
                  </div>
                </div>
                <div className="col-6">
                  <div className="border-bottom">
                    <h1 className="mb-3">Archivos adjuntos</h1>
                    <ul className="list-unstyled lista-botones">
                      <li className="bg-light rounded-pill px-4 fw-bold">
                        Archivo 1
                        <button className="btn">
                          <i className="bi bi-download"></i>
                        </button>
                      </li>
                      <li className="bg-light rounded-pill px-4 fw-bold">
                        Archivo 2
                        <button className="btn">
                          <i className="bi bi-download"></i>
                        </button>
                      </li>
                      <li className="bg-light rounded-pill px-4 fw-bold">
                        Archivo 3
                        <button className="btn">
                          <i className="bi bi-download"></i>
                        </button>
                      </li>
                    </ul>
                  </div>
                  <div>
                    <div className="d-flex justify-content-between align-items-center my-3">
                      <h1>Comentarios</h1>
                      <button className="btn">
                        <i className="bi bi-plus fs-3"></i>
                      </button>
                    </div>

                    <ul className="list-unstyled lista-botones">
                      <li className="bg-light rounded-pill px-4 fw-bold">
                        1 - 02/11/2024 22:35 - Silvia Romero
                        <button className="btn">
                          <i className="bi bi-eye"></i>
                        </button>
                      </li>
                      <li className="bg-light rounded-pill px-4 fw-bold">
                        2 - Fecha y hora - Usuario emisor
                        <button className="btn">
                          <i className="bi bi-eye"></i>
                        </button>
                      </li>
                      <li className="bg-light rounded-pill px-4 fw-bold">
                        3 - Fecha y hora - Usuario emisor
                        <button className="btn">
                          <i className="bi bi-eye"></i>
                        </button>
                      </li>
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
    </Layout>
  );
};

export default Nuevo;
