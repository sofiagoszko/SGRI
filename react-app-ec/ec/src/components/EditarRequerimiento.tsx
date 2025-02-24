import { useState, useEffect } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
import "./Registro.css";

function Login() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [empleado, setEmpleado] = useState({
    nombre: "",
    apellido: "",
    email: "",
    password: "",
    userName: "",
    legajo: "",
    cargo: "",
    departamento: "",
  });

  const {
    nombre,
    apellido,
    email,
    password,
    userName,
    legajo,
    cargo,
    departamento,
  } = empleado;

  useEffect(() => {
    cargarEmpleado();
  }, []);

  const cargarEmpleado = async () => {
    try {
      const token = localStorage.getItem("authToken");
      const resultado = await axios.get(
        `http://localhost:8080/api/usuario-empresa/usuarios/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(resultado.data.data);
      const { password, ...restoEmpleado } = resultado.data.data;
      setEmpleado(restoEmpleado);
    } catch (error) {
      console.error("Error al cargar el ususario:", error);
    }
  };

  const onInputChange = (e) => {
    const { name, value } = e.target;
    setEmpleado((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const onSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("authToken"); // Obtiene el token
      await axios.put(
        `http://localhost:8080/api/usuario-empresa/usuarios/${id}`,
        empleado,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      Swal.fire({
        icon: "success",
        title: "Éxito",
        text: "¡Usuario modificado con éxito!",
      }).then(() => {
        navigate("/home");
      });
    } catch (error) {
      console.error("Error al editar usuario:", error);
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Hubo un error al editar el usuario",
      });
    }
  };

  return (
    <main className="main__registro">
      <div className="bg-white p-5 rounded-5 card__registro">
        <h1 className="mb-4 text-center">Editar Usuario</h1>

        <form onSubmit={onSubmit} className="row justify-content-center">
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="nombre" className="form-label">
              Nombre
            </label>
            <input
              type="text"
              className="form-control"
              id="nombre"
              placeholder="Nombre"
              value={nombre}
              onChange={onInputChange}
              required
              disabled
              name="nombre"
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="apellido" className="form-label">
              Apellido
            </label>
            <input
              type="text"
              className="form-control"
              id="apellido"
              placeholder="Apellido"
              value={apellido}
              onChange={onInputChange}
              required
              disabled
              name="apellido"
            />
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="email" className="form-label">
              Correo electrónico
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              placeholder="Correo electrónico"
              value={email}
              onChange={onInputChange}
              required
              name="email"
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="legajo" className="form-label">
              Legajo
            </label>
            <input
              type="number"
              className="form-control"
              id="legajo"
              value={legajo}
              onChange={onInputChange}
              required
              disabled
              name="legajo"
            />
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="departamento" className="form-label">
              Departamento
            </label>
            <select
              name="departamento"
              id="departamento"
              className="form-select"
              value={departamento}
              onChange={onInputChange}
            >
              <option value="">Seleccione un departamento</option>
              <option value="Departamento 1">Departamento 1</option>
              <option value="Departamento 2">Departamento 2</option>
              <option value="Departamento 3">Departamento 3</option>
              <option value="Departamento 4">Departamento 4</option>
            </select>
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="cargo" className="form-label">
              Cargo
            </label>
            <select
              name="cargo"
              id="cargo"
              className="form-select"
              value={cargo}
              onChange={onInputChange}
            >
              <option value="">Seleccione un cargo</option>
              <option value="Cargo 1">Cargo 1</option>
              <option value="Cargo 2">Cargo 2</option>
              <option value="Cargo 3">Cargo 3</option>
              <option value="Cargo 4">Cargo 4</option>
            </select>
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="userName" className="form-label">
              Usuario
            </label>
            <input
              type="text"
              className="form-control"
              id="userName"
              placeholder="Usuario"
              value={userName}
              onChange={onInputChange}
              disabled
              name="userName"
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="password" className="form-label">
              Nueva Contraseña
            </label>
            <input
              type="password"
              className="form-control"
              id="password"
              placeholder="Nueva contraseña"
              value={password}
              onChange={onInputChange}
              name="password"
            />
          </div>
          <div className="d-flex justify-content-evenly">
            <Link
              to="/home"
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

export default Login;
