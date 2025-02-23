import React, { useState, useEffect } from "react";
import { useAuth } from "../../utils/AuthContext.jsx";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout";
import "./MisAsignaciones.css"; // Importa los estilos CSS
import { tablePaginationClasses } from "@mui/material";
import { Requerimiento } from "../../types/Requerimiento.js";
import axios from "axios";
import ModalDetalleRequerimiento from "../../components/ModalDetalleRequerimiento.js";
const authToken = localStorage.getItem("authToken") || "";
import ColoresEstado from "../../utils/ColoresEstado";

const Nuevo = () => {
  const userId = localStorage.getItem("userId");
  const [mostrarModal, setMostrarModal] = useState(false);
  const [tipos, setTipos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [reqSeleccionado, setReqSeleccionado] = useState<
    Requerimiento | undefined
  >(undefined);
  const [categoriasSeleccionables, setCategoriasSeleccionables] = useState([]);
  const manejadorFiltros = (tipo, valor) => {
    setFiltros({ ...filtros, [tipo]: valor });
  };
  const [filtros, setFiltros] = useState({
    categoriaTipo: "",
    estado: "",
    tipoRequerimiento: "",
    usuarioEmisor: "",
  });
  const restablecerFiltros = () => {
    setFiltros({
      categoriaTipo: "",
      estado: "",
      tipoRequerimiento: "",
      usuarioEmisor: "",
    });
    setCategoriasSeleccionables([]);
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
          }/requerimiento/requerimientos/usuario-destinatario/${userId}`,
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
      req.categoriaTipo == categoriaSeleccionada.descripcion;

    const filtroEstado = filtros.estado === "" || req.estado === filtros.estado;

    const filtroTipo =
      filtros.tipoRequerimiento === "" ||
      req.tipoRequerimiento == filtros.tipoRequerimiento;

    const filtroUsuario =
      filtros.usuarioEmisor === "" ||
      req.usuarioEmisor == filtros.usuarioEmisor;

    return filtroCategoria && filtroEstado && filtroTipo && filtroUsuario;
  });

  const showModal = (requerimiento: Requerimiento) => {
    setReqSeleccionado(requerimiento);
    setMostrarModal(true);
  };
  const closeModal = () => {
    setReqSeleccionado(undefined);
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
            <label htmlFor="tipos">Categoría</label>
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
              className="form-select min-w-select filtros"
              value={filtros.usuarioEmisor}
              onChange={(e) =>
                manejadorFiltros("usuarioEmisor", e.target.value)
              }
            >
              <option value="" hidden></option>
              {usuarios.map((user) => (
                <option value={user?.id || ""} key={user?.id}>
                  {`${user?.nombre} ${user?.apellido}`}
                </option>
              ))}
            </select>
            <label htmlFor="tipos">Asignado por</label>
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
                <th scope="col">Asignado por</th>
                <th scope="col">Fecha de Alta</th>
                <th scope="col">Asunto</th>
                <th scope="col">Categoría</th>
              </tr>
            </thead>
            <tbody>
              {requerimientosFiltrados.map((req) => (
                <tr key={req?.id}>
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
                    {req.tipoRequerimiento}
                  </td>
                  <td scope="col" className="align-middle">
                    {req.usuarioEmisor}
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
      <ModalDetalleRequerimiento
        requerimiento={reqSeleccionado}
        mostrarModal={mostrarModal}
        closeModal={closeModal}
      />
    </Layout>
  );
};

export default Nuevo;
