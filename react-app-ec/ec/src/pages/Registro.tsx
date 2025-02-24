import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./Registro.css";
import Swal from "sweetalert2";

function Login() {
  // Estado para los campos del formulario y el error
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [email, setEmail] = useState("");
  const [legajo, setLegajo] = useState("");
  const [departamento, setDepartamento] = useState("");
  const [cargo, setCargo] = useState("");
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const navigate = useNavigate();

  // Maneja el envío del formulario
  const handleSubmit = async (e: any) => {
    e.preventDefault();
    //setShowAlert(false);
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/usuario-empresa/registracion`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            nombre: nombre,
            apellido: apellido,
            email: email,
            password: password,
            userName: usuario,
            legajo: legajo,
            cargo: cargo,
            departamento: departamento,
          }),
        }
      );

      const data = await response.json();
      console.log("Respuesta del backend:", data);

      if (response.ok) {
        //const data = await response.json();
        console.log(data);
        Swal.fire({
          icon: "success",
          title: "Éxito",
          text: "¡Usuario creado con éxito!",
        }).then(() => {
          navigate("/");
        });
        navigate("/");
      } else {
        setAlertMessage(data.userFriendlyMessage);
        setAlertType("warning");
        setShowAlert(true);
      }
    } catch (error) {
      console.error("Error en creación de requerimiento:", error);
      setAlertMessage("Datos invalidos");
      setAlertType("danger");
      setShowAlert(true);
    }
  };

  return (
    <main className="main__registro">
      <div className="bg-white p-5 rounded-5 card__registro">
        <h1 className="mb-4 text-center">Nuevo usuario</h1>

        {showAlert && alertMessage && (
          <div className={`alert alert-${alertType}`} role="alert">
            {alertMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="row justify-content-center">
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="nombre" className="form-label">
              Nombre <span className="text-danger">*</span>
            </label>
            <input
              type="text"
              className="form-control"
              id="nombre"
              placeholder="Nombre"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              required
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="apellido" className="form-label">
              Apellido <span className="text-danger">*</span>
            </label>
            <input
              type="text"
              className="form-control"
              id="apellido"
              placeholder="Apellido"
              value={apellido}
              onChange={(e) => setApellido(e.target.value)}
              required
            />
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="email" className="form-label">
              Correo electrónico <span className="text-danger">*</span>
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              placeholder="Correo electrónico"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="legajo" className="form-label">
              Legajo <span className="text-danger">*</span>
            </label>
            <input
              type="number"
              className="form-control"
              id="legajo"
              value={legajo}
              onChange={(e) => setLegajo(e.target.value)}
              required
            />
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="departamento" className="form-label">
              Departamento <span className="text-danger">*</span>
            </label>
            <select
              name="departamento"
              id="departamento"
              className="form-select"
              value={departamento}
              onChange={(e) => setDepartamento(e.target.value)}
            >
              <option value="">Seleccione un departamento</option>
              <option value="Departamento 2">Departamento 1</option>
              <option value="Departamento 3">Departamento 2</option>
              <option value="Departamento 4">Departamento 3</option>
              <option value="Departamento 5">Departamento 4</option>
            </select>
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="cargo" className="form-label">
              Cargo <span className="text-danger">*</span>
            </label>
            <select
              name="cargo"
              id="cargo"
              className="form-select"
              value={cargo}
              onChange={(e) => setCargo(e.target.value)}
            >
              <option value="">Seleccione un cargo</option>
              <option value="Cargo 1">Cargo 1</option>
              <option value="Cargo 2">Cargo 2</option>
              <option value="Cargo 3">Cargo 3</option>
              <option value="Cargo 4">Cargo 4</option>
            </select>
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="usuario" className="form-label">
              Usuario <span className="text-danger">*</span>
            </label>
            <input
              type="text"
              className="form-control"
              id="usuario"
              placeholder="Usuario"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              required
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="password" className="form-label">
              Contraseña <span className="text-danger">*</span>
            </label>
            <input
              type="password"
              className="form-control"
              id="password"
              placeholder="Contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="d-flex justify-content-evenly">
            <Link
              to="/"
              className="btn btn-danger text-white align-self-center px-5 mt-3"
            >
              Cancelar
            </Link>
            {/* <Link
              to="/"
              className="btn btn-login-success text-white align-self-center px-5 mt-3"
            >
              Aceptar
            </Link> */}
            <button className="btn btn-login-success text-white align-self-center px-5 mt-3">
              Aceptar
            </button>
          </div>
        </form>
      </div>
    </main>
  );
}

export default Login;
