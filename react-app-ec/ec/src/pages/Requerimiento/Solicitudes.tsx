import React, { useState, useEffect } from "react";
import Layout from "../../components/Layout.js";
import "./MisAsignaciones.css";
import axios from "axios";
import { Requerimiento } from "../../types/Requerimiento.js";
import ColoresEstado from "../../utils/ColoresEstado";
import ModalDetalleRequerimiento from "../../components/ModalDetalleRequerimiento.js";

const ExplorarSolicitudes = () => {
  const authToken = localStorage.getItem("authToken");
  const [reqSeleccionado, setReqSeleccionado] = useState<
    Requerimiento | undefined
  >(undefined);
  const [tipos, setTipos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [fechaDesde, setFechaDesde] = useState("");
  const [fechaHasta, setFechaHasta] = useState("");
  const [categoriasSeleccionables, setCategoriasSeleccionables] = useState([]);
  const manejadorFiltros = (tipo, valor) => {
    setFiltros({ ...filtros, [tipo]: valor });
  };
  const [filtros, setFiltros] = useState({
    categoriaTipo: "",
    estado: "",
    tipoRequerimiento: "",
    usuarioDestinatario: "",
    fechaDesde: "",
    fechaHasta: "",
  });
  const restablecerFiltros = () => {
    setFiltros({
      categoriaTipo: "",
      estado: "",
      tipoRequerimiento: "",
      usuarioDestinatario: "",
      fechaDesde: "",
      fechaHasta: "",
    });
    setFechaDesde("");
    setFechaHasta("");
  };

  const [mostrarModal, setMostrarModal] = useState(false);
  const [requerimientos, setRequerimientos] = useState<Requerimiento[]>([]);
  const [usuariosDestinatarios, setUsuariosDestinatarios] = useState<{
    [key: number]: UsuarioDestinatario;
  }>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [paginaActual, setPaginaActual] = useState(1);
  const [itemsPorPagina] = useState(5);

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
        .get(`${import.meta.env.VITE_API_URL}/requerimiento/requerimientos`, {
          headers: {
            Authorization: `Bearer ${authToken}`,
          },
        })
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

    const filtroDesde =
      !filtros.fechaDesde ||
      new Date(req.fechaHora) >= new Date(filtros.fechaDesde);

    const filtroHasta =
      !filtros.fechaHasta ||
      new Date(req.fechaHora) <= new Date(filtros.fechaHasta);

    return (
      filtroCategoria &&
      filtroEstado &&
      filtroTipo &&
      filtroUsuario &&
      filtroDesde &&
      filtroHasta
    );
  });
  const showModal = (requerimiento: Requerimiento) => {
    setReqSeleccionado(requerimiento);
    setMostrarModal(true);
  };
  const closeModal = () => {
    setReqSeleccionado(undefined);
    setMostrarModal(false);
  };

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
        <h2>Explorar Solicitudes</h2>

        {/* Filtros */}
        <div className="d-flex flex-wrap gap-3 justify-content-between mb-4">
          <div className="d-flex flex-wrap gap-3 justify-content-between flex-grow-1">
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
              <label htmlFor="estados">Estado</label>
            </div>
            <div className="form-floating">
              <select
                name="Propietario"
                id="Propietario"
                className="form-select min-w-select filtros"
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
              <label htmlFor="propietario">Propietario</label>
            </div>
            <div className="form-floating">
              <input
                type="date"
                id="fecha_desde"
                value={fechaDesde}
                className="form-control min-w-select filtros"
                onChange={(e) => {
                  manejadorFiltros("fechaDesde", e.target.value);
                  setFechaDesde(e.target.value);
                }}
              />
              <label htmlFor="fecha_desde">Fecha Desde</label>
            </div>
            <div className="form-floating">
              <input
                type="date"
                id="fecha_hasta"
                value={fechaHasta}
                className="form-control min-w-select filtros"
                onChange={(e) => {
                  manejadorFiltros("fechaHasta", e.target.value);
                  setFechaHasta(e.target.value);
                }}
              />
              <label htmlFor="fecha_hasta">Fecha Hasta</label>
            </div>
          </div>

          <div className="d-flex justify-content-center">
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
                <th scope="col">Propietario</th>
                <th scope="col">Fecha de Alta</th>
                <th scope="col">Asunto</th>
                <th scope="col">Categoría</th>
              </tr>
            </thead>
            <tbody>
              {currentRequerimientos.map((req, index) => (
                <tr key={index}>
                  <td scope="col">
                    <div className="d-flex gap-2 align-items-center">
                      {req.codigo}
                      <button className="btn" onClick={() => showModal(req)}>
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
                    {req.tipoRequerimiento.codigo}
                  </td>
                  <td scope="col" className="align-middle">
                    {`${req.usuarioDestinatario.nombre} ${req.usuarioDestinatario.apellido}`}
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

export default ExplorarSolicitudes;
