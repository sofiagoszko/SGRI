import React, { useState, useEffect } from "react";
import { useAuth } from "../../utils/AuthContext.jsx";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout.js";

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
        const [requerimientos, setRequerimientos] = useState([
          {
            codigo: "GOP-2024-0000099716",
            estado: "Asignado",
            prioridad: "ALTA",
            tipoRequerimiento: "GOP",
            usuarioDestinatario: "Aldo",  
            fechaHora: "19/11/2024",
            asunto: "Análisis",
            categoriaTipo: "Hardware",
          },
          {
            codigo: "ERR-2024-0000099716",
            estado: "Abierto",
            prioridad: "BAJA",
            tipoRequerimiento: "ERR",
            usuarioDestinatario: "Seba",
            fechaHora: "19/11/2024",
            asunto: "Análisis",
            categoriaTipo: "Software",
          }, 
          {
            codigo: "GOP-2024-0000099716",
            estado: "Asignado",
            prioridad: "URGENTE",
            tipoRequerimiento: "GOP",
            usuarioDestinatario: "Aldo",
            fechaHora: "19/11/2024",
            asunto: "Análisis",
            categoriaTipo: "Red",
          }, 
          {
            codigo: "ERR-2024-0000099716",
            estado: "Abierto",
            prioridad: "ALTA",
            tipoRequerimiento: "ERR",
            usuarioDestinatario: "Seba",
            fechaHora: "19/11/2024",
            asunto: "Análisis",
            categoriaTipo: "Red",
          }, 
          {
            codigo: "GOP-2024-0000099716",
            estado: "Asignado",
            prioridad: "MEDIA",
            tipoRequerimiento: "GOP",
            usuarioDestinatario: "Aldo",
            fechaHora: "19/11/2024",
            asunto: "Análisis",
            categoriaTipo: "Software",
          },
          {
            codigo: "ERR-2024-0000099716",
            estado: "Abierto",
            prioridad: "ALTA",
            tipoRequerimiento: "ERR",
            usuarioDestinatario: "Seba",
            fechaHora: "19/11/2024",
            asunto: "Análisis",
            categoriaTipo: "Hardware",
          },
        ]);
        const requerimientosFiltrados = requerimientos.filter((req) => {
          const filtroCategoria = filtros.categoriaTipo === "" || req.categoriaTipo === filtros.categoriaTipo;
          const filtroEstado = filtros.estado === "" || req.estado === filtros.estado;
          const filtroTipo = filtros.tipoRequerimiento === "" || req.tipoRequerimiento === filtros.tipoRequerimiento;
          const filtroUsuario = filtros.usuarioDestinatario === "" || req.usuarioDestinatario === filtros.usuarioDestinatario;
          return filtroCategoria && filtroEstado && filtroTipo && filtroUsuario;
        });
        const showModal = () => {
          setMostrarModal(true);
        };
        const closeModal = () => {
          setMostrarModal(false);
        };
        const coloresEstado = {
          BAJA: "text-primary",
          MEDIA: "text-muted",
          ALTA: "text-warning",
          URGENTE: "text-danger",
        };
  return (
    <Layout>
      <section className="content-placeholder bg-white rounded-4 align-self-center flex-grow-1 mb-5 p-5">
        <h2>Mis Solicitudes</h2>

        {/* Filtros */}
      <div className="d-flex flex-wrap gap-3 justify-content-between mb-4">
      <div className="d-flex flex-wrap gap-3">
      <div className="form-floating">
      <select
        name="tipos"
        id="tipos"
        className="form-select min-w-select"
        value={filtros.tipoRequerimiento}
        onChange={(e) => manejadorFiltros("tipoRequerimiento", e.target.value)}
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
        onChange={(e) => manejadorFiltros("categoriaTipo", e.target.value)}
      >
        <option value=""></option>
        <option value="Hardware">Hardware</option>
        <option value="Software">Software</option>
        <option value="Red">Red</option>
        <option value="Seguridad">Seguridad</option>
      </select>
      <label htmlFor="categorias">Todas las categorías</label>
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
      <label htmlFor="estados">Todos los estados</label>
      </div>
      <div className="form-floating">
      <select
        name="propietario"
        id="propietario"
        className="form-select min-w-select"
        value={filtros.usuarioDestinatario}
        onChange={(e) => manejadorFiltros("usuarioDestinatario", e.target.value)}
      >
        <option value=""></option>
        <option>Sofia</option>
        <option>Aldo</option>
        <option>Seba</option>
        <option>Silvia</option>
      </select>
      <label htmlFor="propietario">Todos los propietarios</label>
      </div>
      </div>
      <div>
      <button className="btn btn-secondary" onClick={restablecerFiltros}>
        Desfiltrar
      </button>
      </div>
      </div>

      {/* Segunda línea con los filtros de fecha */}
      <div className="d-flex flex-wrap gap-3 justify-content-start mb-4">
      <div className="form-floating">
      <input
      type="date"
      id="fecha_desde"
      className="form-control min-w-select"
      onChange={(e) => manejadorFiltros("fechaDesde", e.target.value)}
      />
      <label htmlFor="fecha_desde">Fecha Desde</label>
      </div>
      <div className="form-floating">
      <input
      type="date"
      id="fecha_hasta"
      className="form-control min-w-select"
      onChange={(e) => manejadorFiltros("fechaHasta", e.target.value)}
      />
      <label htmlFor="fecha_hasta">Fecha Hasta</label>
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
                    <span className={coloresEstado[req.prioridad!]}>
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
    </Layout>
  );
};

export default Nuevo;