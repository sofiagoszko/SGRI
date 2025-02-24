import React, { useState, useEffect } from "react";
import { useAuth } from "../../utils/AuthContext.jsx";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout.js";
import "./MisAsignaciones.css";
import { Requerimiento } from "../../types/Requerimiento.js";
import axios from "axios";
import ModalDetalleRequerimiento from "../../components/ModalDetalleRequerimiento.js";
import ColoresEstado from "../../utils/ColoresEstado";

const MisSolicitudes = () => {

  const navigate = useNavigate();
  const irAEditarRequerimiento = (id) => {
    navigate(`/requerimiento/editar/${id}`, { state: { from: "/requerimiento/mis-solicitudes" } });
  };
  const authToken = localStorage.getItem("authToken") || "";
  const userId = localStorage.getItem("userId");
  const [reqSeleccionado, setReqSeleccionado] = useState<
    Requerimiento | undefined
  >(undefined);
  const [tipos, setTipos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [categoriasSeleccionables, setCategoriasSeleccionables] = useState([]);
  const [mostrarModal, setMostrarModal] = useState(false);
  const manejadorFiltros = (tipo, valor) => {
    setFiltros((prevFiltros) => ({
      ...prevFiltros,
      [tipo]: valor,
    }));
    setPaginaActual(1);
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
    setCategoriasSeleccionables([]);
    setPaginaActual(1);
  };
  const [requerimientos, setRequerimientos] = useState<Requerimiento[]>([]);

  useEffect(() => {
    if (authToken) {
      axios
        .get(`${import.meta.env.VITE_API_URL}/tipo-requerimiento/tipos`, {
          headers: {
            Authorization: `Bearer ${authToken}`,
          },
        })
        .then((res) => {
          setTipos(res.data);
        });
      axios
        .get(`${import.meta.env.VITE_API_URL}/usuario-empresa/usuarios`, {
          headers: {
            Authorization: `Bearer ${authToken}`,
          },
        })
        .then((res) => {
          setUsuarios(res.data.data);
        });

      axios
        .get(
          `${
            import.meta.env.VITE_API_URL
          }/requerimiento/requerimientos/usuario-emisor/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${authToken}`,
            },
          }
        )
        .then((res) => {
          if (res.status === 200) setRequerimientos(res.data.data);
        });
    }
  }, []);

  const requerimientosFiltrados = requerimientos.filter((req) => {
    const categoriaSeleccionada = categoriasSeleccionables.find(
      (catSel) => filtros.categoriaTipo == catSel.id
    );

    const filtroCategoria =
      filtros.categoriaTipo === "" ||
      (categoriaSeleccionada &&
        req.categoriaTipo == categoriaSeleccionada.descripcion);

    const filtroEstado = filtros.estado === "" || req.estado === filtros.estado;

    const filtroTipo =
      filtros.tipoRequerimiento === "" ||
      req.tipoRequerimiento.id == filtros.tipoRequerimiento;

    const filtroUsuario =
      filtros.usuarioDestinatario === "" ||
      req.usuarioDestinatario.id == filtros.usuarioDestinatario;

    return filtroCategoria && filtroEstado && filtroTipo && filtroUsuario;
  }).sort((a, b) => new Date(b.fechaHora) - new Date(a.fechaHora)); 

  
  const showModal = (requerimiento: Requerimiento) => {
    setReqSeleccionado(requerimiento);
    setMostrarModal(true);
  };
  const closeModal = () => {
    setReqSeleccionado(undefined);
    setMostrarModal(false);
  };
  const [paginaActual, setPaginaActual] = useState(1);
  const [itemsPorPagina] = useState(5);
  const idexUltimoItem = paginaActual * itemsPorPagina;
  const idexPrimerItem = idexUltimoItem - itemsPorPagina;
  const currentRequerimientos = requerimientosFiltrados.slice(
    idexPrimerItem,
    idexUltimoItem
  );
  const pageNumbers = [];
  for (
    let i = 1;
    i <= Math.ceil(requerimientosFiltrados.length / itemsPorPagina);
    i++
  ) {
    pageNumbers.push(i);
  }
  return (
    <Layout>
      <section className="content-placeholder bg-white rounded-4 align-self-center flex-grow-1 mb-5 p-5">
        <h2>Mis Solicitudes</h2>
        {/* Filtros */}
        <div className="overflow-x-auto d-flex justify-content-evenly gap-3 mb-4">
          <div className="form-floating">
            <select
              name="tipos"
              id="tipos"
              className="form-select min-w-select filtros"
              value={filtros.tipoRequerimiento}
              onChange={(e) => {
                let tipoSeleccionado = tipos.find(
                  (tipo) => tipo.id == e.target.value
                );
                if (tipoSeleccionado) {
                  manejadorFiltros("tipoRequerimiento", e.target.value);
                  setCategoriasSeleccionables(tipoSeleccionado?.categorias);
                }
              }}
            >
              <option value="" hidden></option>
              {tipos.map((tipo) => (
                <option value={tipo?.id || ""} key={tipo?.id}>
                  {tipo?.codigo || ""}
                </option>
              ))}
            </select>
            <label htmlFor="tipos">Tipo</label>
          </div>
          <div className="form-floating">
            <select
              name="categorias"
              id="categorias"
              className="form-select min-w-select filtros"
              value={filtros.categoriaTipo}
              onChange={(e) =>
                manejadorFiltros("categoriaTipo", e.target.value)
              }
              disabled={!categoriasSeleccionables.length}
            >
              <option value="" hidden></option>
              {categoriasSeleccionables &&
                categoriasSeleccionables.map((tipo) => (
                  <option value={tipo?.id || ""} key={tipo?.id}>
                    {tipo?.descripcion || ""}
                  </option>
                ))}
            </select>
            <label htmlFor="categorias">Categoria</label>
          </div>
          <div className="form-floating">
            <select
              name="estados"
              id="estados"
              className="form-select min-w-select filtros"
              value={filtros.estado}
              onChange={(e) => manejadorFiltros("estado", e.target.value)}
            >
              <option value="" hidden></option>
              <option value="Abierto">Abierto</option>
              <option value="Asignado">Asignado</option>
            </select>
            <label htmlFor="tipos">Estado</label>
          </div>
          <div className="form-floating">
            <select
              name="Propietario"
              id="Propietario"
              className="form-select min-w-sele() => ct filtros"
              value={filtros.usuarioDestinatario}
              onChange={(e) =>
                manejadorFiltros("usuarioDestinatario", e.target.value)
              }
            >
              <option value="" hidden></option>
              {usuarios.map((user) => (
                <option value={user?.id || ""} key={user?.id}>
                  {`${user?.nombre} ${user?.apellido}`}
                </option>
              ))}
            </select>
            <label htmlFor="tipos">Destinatario</label>
          </div>
          <div className="d-flex justify-content-center">
            {" "}
            {/* Centrar el botón */}
            <button
              className="btn btn-secondary boton"
              onClick={restablecerFiltros}
            >
              Limpiar
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
                <th scope="col">Asignado a</th>
                <th scope="col">Fecha de Alta</th>
                <th scope="col">Asunto</th>
                <th scope="col">Categoría</th>
              </tr>
            </thead>
            <tbody>
              {currentRequerimientos.map((req) => (
                <tr key={req?.id}>
                  <td scope="col">
                    <div className="d-flex gap-2 align-items-center">
                      {req.codigo}
                      <button className="btn" onClick={() => showModal(req)}>
                        <i className="bi bi-eye"></i>
                      </button>
                      <button 
                        className="btn btn-sm me-3" 
                        onClick={() => irAEditarRequerimiento(req.id)}
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-pencil-fill" viewBox="0 0 16 16">
                          <path d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.5.5 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11z"/>
                        </svg>
                    </button>
                    </div>
                  </td>
                  <td scope="col" className="align-middle">
                    <div className="d-flex gap-2 align-items-center">
                      {req.estado}
                    </div>
                  </td>
                  <td scope="col" className="align-middle">
                    <span className={ColoresEstado[req.prioridad!]}>
                      {req.prioridad}
                    </span>
                  </td>
                  <td scope="col" className="align-middle">
                    {req.tipoRequerimiento.codigo}
                  </td>
                  <td scope="col" className="align-middle">
                    {req.usuarioDestinatario
                      ? `${req.usuarioDestinatario.nombre} ${req.usuarioDestinatario.apellido}`
                      : " "}
                  </td>
                  <td scope="col" className="align-middle">
                    {new Date(req.fechaHora).toLocaleDateString()}
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
        {/* Paginación */}
        <div>
          <ul className="pagination pagination-sm">
            <li className={`page-item ${paginaActual === 1 ? "disabled" : ""}`}>
              <button
                className="page-link"
                onClick={() => setPaginaActual(paginaActual - 1)}
                disabled={paginaActual === 1}
              >
                <span aria-hidden="true">&laquo;</span>
              </button>
            </li>
            {pageNumbers.map((number) => (
              <li
                key={number}
                className={`page-item ${
                  paginaActual === number ? "active" : ""
                }`}
              >
                <button
                  className="page-link"
                  onClick={() => setPaginaActual(number)}
                >
                  {number}
                </button>
              </li>
            ))}
            <li
              className={`page-item ${
                paginaActual === pageNumbers.length ? "disabled" : ""
              }`}
            >
              <button
                className="page-link"
                onClick={() => setPaginaActual(paginaActual + 1)}
                disabled={paginaActual === pageNumbers.length}
              >
                <span aria-hidden="true">&raquo;</span>
              </button>
            </li>
          </ul>
        </div>
      </section>
      <ModalDetalleRequerimiento
        requerimiento={reqSeleccionado}
        mostrarModal={mostrarModal}
        closeModal={closeModal}
      />
    </Layout>
  );
};

export default MisSolicitudes;
