import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./Registro.css";

function Login() {
  // Estado para los campos del formulario y el error
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const navigate = useNavigate();

  const AUTH_ENDPOINT = "/api/auth/credentials"; // Constante para la URL de autenticación

  // Maneja el envío del formulario
  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setShowAlert(false);
    navigate("/home");
    // try {
    //     const response = await fetch(`${endpoint}${AUTH_ENDPOINT}`, {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json',
    //         },
    //         body: JSON.stringify({
    //             email: email,
    //             password: password,
    //         }),
    //     });

    //     if (response.ok) {
    //         const data = await response.json();
    //         const authToken = data.data;
    //         localStorage.setItem('authToken', authToken);
    //         navigate('/MisExamenes'); // Redirige a Mis Exámenes
    //     } else {
    //         setAlertMessage('Email o contraseña invalidos')
    //         setAlertType('warning')
    //         setShowAlert(true); // Muestra un mensaje de error más descriptivo
    //     }
    // } catch (error) {
    //     setAlertMessage('Email o contraseña invalidos')
    //     setAlertType('warning')
    //     setShowAlert(true);// Muestra un error en caso de fallo del servidor
    // }
  };

  return (
    <main className="main__registro">
      <div className="bg-white p-5 rounded-5 card__registro">
        <h1 className="mb-4 text-center">Nuevo usuario</h1>
        <h2 className="text-center fw-normal h3 mb-5">Ingrese sus datos</h2>
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
            />
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="legajo" className="form-label">
              Legajo <span className="text-danger">*</span>
            </label>
            <input type="number" className="form-control" id="legajo" />
          </div>
          <div className="mb-3 col-12 col-md-5">
            <label htmlFor="departamento" className="form-label">
              Departamento <span className="text-danger">*</span>
            </label>
            <select
              name="departamento"
              id="departamento"
              className="form-select"
            >
              <option>Departamento 1</option>
              <option>Departamento 2</option>
              <option>Departamento 3</option>
              <option>Departamento 4</option>
            </select>
          </div>
          <div className="mb-3 col-12 offset-md-1 col-md-5">
            <label htmlFor="cargo" className="form-label">
              Cargo <span className="text-danger">*</span>
            </label>
            <select name="cargo" id="cargo" className="form-select">
              <option>Cargo 1</option>
              <option>Cargo 2</option>
              <option>Cargo 3</option>
              <option>Cargo 4</option>
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
            />
          </div>
          <div className="d-flex justify-content-evenly">
            <Link
              to="/"
              className="btn btn-danger text-white align-self-center px-5 mt-3"
            >
              Cancelar
            </Link>
            <Link
              to="/"
              className="btn btn-login-success text-white align-self-center px-5 mt-3"
            >
              Aceptar
            </Link>
          </div>
        </form>
      </div>
    </main>
  );
}

export default Login;
