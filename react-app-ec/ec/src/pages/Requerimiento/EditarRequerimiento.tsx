import { useState, useEffect } from "react";
import { useNavigate, Link, useParams, useLocation } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";

//import "./Registro.css";

function EditarRequerimiento() {
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams();
  const origen = location.state?.from || "/home";

  const[usuarios, setUsuarios] = useState([]);
  const [requerimiento, setRequerimiento] = useState({
    estado: "",
    prioridad: "",
    fechaHora: "",
    asunto: "",
    descripcion: "",
    categoriaTipo: "",
    tipoRequerimiento: "",
    usuarioEmisor: "",
    usuarioDestinatario:"",
  });

  const { estado, prioridad, fechaHora, asunto, descripcion, categoriaTipo, tipoRequerimiento, usuarioEmisor, usuarioDestinatario} = requerimiento;

  useEffect(() => {
    cargarRequerimiento();
  }, []);

  const cargarRequerimiento = async () => {
    try {
      const token = localStorage.getItem("authToken");
      const resultado = await axios.get(
        `${import.meta.env.VITE_API_URL}/requerimiento/requerimientos/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
  
      console.log(resultado.data.data);
      const data = resultado.data.data;
  
      setRequerimiento({
        estado: data.estado || "",
        prioridad: data.prioridad || "",
        fechaHora: data.fechaHora || "",
        asunto: data.asunto || "",
        descripcion: data.descripcion || "",
        categoriaTipo: data.categoriaTipo || "",
        tipoRequerimiento: data.tipoRequerimiento?.codigo || "",
        tipoRequerimientoId: data.tipoRequerimiento?.id || "",
        usuarioEmisor: `${data.usuarioEmisor?.nombre || ""} ${data.usuarioEmisor?.apellido || ""}`,
        usuarioEmisorId: data.usuarioEmisor?.id || "",
        usuarioDestinatario: data.usuarioDestinatario?.id || "",
      });
  
      const resUsuarios = await axios.get(
        `${import.meta.env.VITE_API_URL}/usuario-empresa/usuarios`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setUsuarios(resUsuarios.data.data);
    } catch (error) {
      console.error("Error al cargar el usuario:", error);
    }
  };

  const onInputChange = (e) => {
    const { name, value } = e.target;
    setRequerimiento((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const onSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("authToken");
      const updatedEstado = usuarioDestinatario && estado === "Abierto" ? "Asignado" : estado;
      const dataToSend = {
        ...requerimiento,
        estado: updatedEstado,
        tipoRequerimiento:requerimiento.tipoRequerimientoId,
        usuarioEmisor: requerimiento.usuarioEmisorId,
        usuarioDestinatario: requerimiento.usuarioDestinatario,
      };
      console.log(dataToSend);
      await axios.put(
        `${import.meta.env.VITE_API_URL}/requerimiento/requerimientos/${id}`,
        dataToSend,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      Swal.fire({
        icon: "success",
        title: "Éxito",
        text: "Requerimiento modificado con éxito!",
      }).then(() => {
        navigate(origen);
      });
    } catch (error) {
      console.error("Error al editar requerimiento:", error);
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Hubo un error al editar el requerimiento",
      });
    }
  };

  return (
    <main className="main__registro">
      <div className="bg-white p-5 rounded-5 card__registro">
        <h1 className="mb-4 text-center">Editar Requerimiento</h1>

        <form onSubmit={onSubmit} className="row justify-content-center">
          <div className="row g-3">
            <div className="col-md-6">
            <label htmlFor="estado" className="form-label">
              Estado
            </label>
            <select
              name="estado"
              id="estado"
              className="form-select"
              value={estado}
              onChange={onInputChange}
            >
              <option value="Abierto">Abierto</option>
              <option value="Asignado">Asignado</option>  {/* Añadido "Asignado" */}
              <option value="Cerrado">Cerrado</option>
            </select>
            </div>
            <div className="col-md-6">
              <label htmlFor="prioridad" className="form-label">
                Prioridad
              </label>
              <input
                type="text"
                className="form-control"
                id="prioridad"
                placeholder="Apellido"
                value={prioridad}
                required
                disabled
                name="prioridad"
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="asunto" className="form-label">
                Asunto
              </label>
              <input
                type="text"
                className="form-control"
                id="asunto"
                value={asunto}
                disabled
                required
                name="asunto"
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="descripcion" className="form-label">
                Descripción
              </label>
              <input
                type="text"
                className="form-control"
                id="descripcion"
                value={descripcion}
                required
                disabled
                name="descripcion"
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="tipoRequerimiento" className="form-label">
                Tipo
              </label>
              <input
                type="text"
                className="form-control"
                id="tipoRequerimiento"
                value={tipoRequerimiento}
                required
                disabled
                name="tipoRequerimiento"
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="categoriaTipo" className="form-label">
                Categoría
              </label>
              <input
                type="text"
                className="form-control"
                id="categoriaTipo"
                value={categoriaTipo}
                required
                disabled
                name="categoriaTipo"
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="usuarioEmisor" className="form-label">
                Ususario Emisor
              </label>
              <input
                type="text"
                className="form-control"
                id="usuarioEmisor"
                value={usuarioEmisor}
                required
                disabled
                name="usuarioEmisor"
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="usuarioDestinatario" className="form-label">
                Usuario Destinatario
              </label>
              <select
                className="form-control"
                id="usuarioDestinatario"
                name="usuarioDestinatario"
                value={usuarioDestinatario}
                onChange={onInputChange}
              >
                <option value="" hidden>
                  Destinatario
                </option>
                {usuarios &&
                  usuarios.map((user) => (
                    <option value={user.id} key={user.id}>
                      {`${user.nombre} ${user.apellido}`}
                    </option>
                  ))}
               </select>
            </div>
          </div>
          <div className="d-flex justify-content-evenly  mt-4">
            <Link
              to={origen}
              className="btn btn-danger text-white align-self-center px-5 mt-3"
            >
              Cancelar
            </Link>
            <button
              type="submit"
              className="btn btn-login-success text-white align-self-center px-5 mt-3"
            >
              Aceptar
            </button>
          </div>
        </form>
      </div>
    </main>
  );
}

export default EditarRequerimiento;
