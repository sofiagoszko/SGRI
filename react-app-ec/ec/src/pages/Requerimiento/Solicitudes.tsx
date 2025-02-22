import React, { useState, useEffect } from "react";
import { useAuth } from "../../utils/AuthContext.jsx";
//import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout.js";
import "./MisAsignaciones.css";
import axios from "axios";

interface Requerimiento {
  id: number;
  codigo: string;
  estado: string;
  prioridad: string;
  tipoRequerimiento: number;
  usuarioDestinatario: number;
  fechaHora: string;
  asunto: string;
  categoriaTipo: string;
}

interface TipoRequerimiento {
  id: number;
  codigo: string;
  descripcion: string;
}

interface UsuarioDestinatario{
  id: number;
  userName: string;
}


const ExplorarSolicitudes = () => {
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

  const [mostrarModal, setMostrarModal] = useState(false);
  const [requerimientos, setRequerimientos] = useState<Requerimiento[]>([]);
  const [tipos, setTipos] = useState<{ [key: number]: TipoRequerimiento }>({});
  const [usuariosDestinatarios, setUsuariosDestinatarios] = useState<{ [key: number]: UsuarioDestinatario }>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [paginaActual, setPaginaActual] = useState(1); 
  const [itemsPorPagina] = useState(5);  



  useEffect(() => {
    const fetchRequerimientos = async () => {
      try {
        const token = localStorage.getItem("authToken");
        const { data } = await axios.get(`http://localhost:8080/api/requerimiento/requerimientos`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (data.status === "Success") {
          setRequerimientos(data.data);
          fetchTiposRequerimientos(data.data);
          fetchUsuariosDetinatarios(data.data);
        } else {
          setError("Error al obtener los requerimientos");
        }
      } catch (err) {
        setError("Error de conexión con el servidor");
      } finally {
        setLoading(false);
      }
    };

    fetchRequerimientos();
  }, []);

  const fetchTiposRequerimientos = async (requerimientos: Requerimiento[]) => {
    const tiposUnicos = new Set(requerimientos.map((req) => req.tipoRequerimiento));
    const nuevosTipos: { [key: number]: TipoRequerimiento } = {};

    await Promise.all(
      Array.from(tiposUnicos).map(async (tipoId) => {
        try {
          const token = localStorage.getItem("authToken");
          const { data } = await axios.get(`http://localhost:8080/api/tipo-requerimiento/tipos/${tipoId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (data.status === "Success") {
            nuevosTipos[tipoId] = data.data;
          }
        } catch (error) {
          console.error(`Error obteniendo tipo de requerimiento ${tipoId}:`, error);
        }
      })
    );

    setTipos(nuevosTipos);
  };

  const fetchUsuariosDetinatarios = async (requerimientos: Requerimiento[]) => {
    const usuariosUnicos = new Set(requerimientos.map((req) => req.usuarioDestinatario));
    const nuevosUsuarios: { [key: number]: { id: number; userName: string } } = {};

    await Promise.all(
      Array.from(usuariosUnicos).map(async (usuarioId) => {
        try {
          const token = localStorage.getItem("authToken");
          const { data } = await axios.get(`http://localhost:8080/api/usuario-empresa/usuarios/${usuarioId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (data.status === "Success") {
            nuevosUsuarios[usuarioId] = data.data;
          }
        } catch (error) {
          console.error(`Error obteniendo usuario ${usuarioId}:`, error);
        }
      })
    );

    setUsuariosDestinatarios(nuevosUsuarios);
  };

  if (loading) return <p>Cargando...</p>;
  if (error) return <p>{error}</p>;

  const requerimientosFiltrados = requerimientos.filter((req) => {
    const filtroCategoria = !filtros.categoriaTipo || req.categoriaTipo === filtros.categoriaTipo;
    const filtroEstado = !filtros.estado || req.estado === filtros.estado;
    const filtroTipo =
      !filtros.tipoRequerimiento || tipos[req.tipoRequerimiento].codigo  === filtros.tipoRequerimiento;
    const filtroUsuario = !filtros.usuarioDestinatario || usuariosDestinatarios[req.usuarioDestinatario].userName === filtros.usuarioDestinatario;

    return filtroCategoria && filtroEstado && filtroTipo && filtroUsuario;
  });
  const showModal = () => {
    setMostrarModal(true);
  };
  const closeModal = () => {
    setMostrarModal(false);
  };
  const coloresEstado = {
    Baja: "text-primary",
    Media: "text-muted",
    Alta: "text-warning",
    Urgente: "text-danger",
  };


  const idexUltimoItem = paginaActual * itemsPorPagina;
  const idexPrimerItem = idexUltimoItem - itemsPorPagina;
  const currentRequerimientos = requerimientosFiltrados.slice(idexPrimerItem, idexUltimoItem);

  const pageNumbers = [];
  for (let i = 1; i <= Math.ceil(requerimientosFiltrados.length / itemsPorPagina); i++) {
    pageNumbers.push(i);
  }

  return (
    <Layout>
      <section className="content-placeholder bg-white rounded-4 align-self-center flex-grow-1 mb-5 p-5">
        <h2>Explorar Solicitudes</h2>

        {/* Filtros */}
      <div className="d-flex flex-wrap gap-3 justify-content-between mb-4">
        <div className="d-flex flex-wrap gap-3">
        <div className="form-floating">
        <select
          name="tipos"
          id="tipos"
          className="form-select min-w-select filtros"
          value={filtros.tipoRequerimiento}
          onChange={(e) => manejadorFiltros("tipoRequerimiento", e.target.value)}
        >
          <option value=""></option>
          <option value="REH">REH</option>
          <option value="ERR">ERR</option>
          <option value="GOP">GOP</option>
        </select>
        <label htmlFor="tipos">Tipo</label>
        </div>
        <div className="form-floating">
        <select
          name="categorias"
          id="categorias"
          className="form-select min-w-select filtros"
          value={filtros.categoriaTipo}
          onChange={(e) => manejadorFiltros("categoriaTipo", e.target.value)}
        >
          <option value=""></option>
          <option value="Hardware">Hardware</option>
          <option value="Software">Software</option>
          <option value="Red">Red</option>
          <option value="Seguridad">Seguridad</option>
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
          <option value=""></option>
          <option>Abierto</option>
          <option>Asignado</option>
        </select>
        <label htmlFor="estados">Estado</label>
        </div>
        <div className="form-floating">
        <select
          name="propietario"
          id="propietario"
          className="form-select min-w-select filtros"
          value={filtros.usuarioDestinatario}
          onChange={(e) => manejadorFiltros("usuarioDestinatario", e.target.value)}
        >
          <option value=""></option>
          <option>Sofia</option>
          <option>Aldo</option>
          <option>Seba</option>
          <option>Silvia</option>
        </select>
        <label htmlFor="propietario">Propietario</label>
        </div>
        </div>
        <div className="form-floating">
          <input
          type="date"
          id="fecha_desde"
          className="form-control min-w-select filtros"
          onChange={(e) => manejadorFiltros("fechaDesde", e.target.value)}
          />
          <label htmlFor="fecha_desde">Fecha Desde</label>
        </div>
        <div className="form-floating">
        <input
          type="date"
          id="fecha_hasta"
          className="form-control min-w-select filtros"
          onChange={(e) => manejadorFiltros("fechaHasta", e.target.value)}
          />
          <label htmlFor="fecha_hasta">Fecha Hasta</label>
        </div>
        <div className="d-flex justify-content-center">
          <button className="btn btn-secondary boton" onClick={restablecerFiltros}>
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
                    {tipos[req.tipoRequerimiento]?.codigo || "Cargando..."}
                  </td>
                  <td scope="col" className="align-middle">
                    {usuariosDestinatarios[req.usuarioDestinatario]?.userName || "Cargando..."}
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
            <li className={`page-item ${paginaActual === 1 ? 'disabled' : ''}`}>
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
                className={`page-item ${paginaActual === number ? 'active' : ''}`}
              >
                <button
                  className="page-link"
                  onClick={() => setPaginaActual(number)}
                >
                  {number}
                </button>
              </li>
            ))}
            <li className={`page-item ${paginaActual === pageNumbers.length ? 'disabled' : ''}`}>
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
    </Layout>
  );
};

export default ExplorarSolicitudes;