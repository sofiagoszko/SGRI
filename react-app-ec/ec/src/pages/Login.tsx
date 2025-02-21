import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./Login.css";
import getBaseUrl from "../utils/getBaseUrl.js";

function Login() {
  // Estado para los campos del formulario y el error
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const navigate = useNavigate();


  const AUTH_ENDPOINT = `${getBaseUrl()}/api/usuario-empresa/credenciales`; // Constante para la URL de autenticación

  // Maneja el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    setShowAlert(false);
    // navigate("/home");
    try {
      console.log(AUTH_ENDPOINT)
        const response = await fetch("http://localhost:8080/api/usuario-empresa/credenciales", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userName: usuario,
                password: password,
            }),
        });

        if (response.ok) {
            const data = await response.json();
            const authToken = data.data;
            localStorage.setItem('authToken', authToken);
            navigate('/home'); // Redirige a home
        } else {
            setAlertMessage('Email o contraseña invalidos')
            setAlertType('warning')
            setShowAlert(true); // Muestra un mensaje de error más descriptivo
        }
    } catch (error) {
      console.error("Error en la autenticación:", error.message);  
      setAlertMessage('Email o contraseña invalidos')
        setAlertType('warning')
        setShowAlert(true);// Muestra un error en caso de fallo del servidor
    }
  };

  return (
    <main className="main__login">
      <div className="bg-white p-5 rounded-5 card__login">
        <h1 className="mb-4 text-center">Iniciar sesión</h1>
        <form onSubmit={handleSubmit} className="d-flex flex-column">
          <div className="mb-3">
            <label htmlFor="usuario" className="form-label">
              Usuario
            </label>
            <input
              type="text"
              className="form-control rounded-3"
              id="usuario"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              className="form-control rounded-3"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="d-flex justify-content-evenly">
            <button className="btn btn-login-success text-white px-5 mt-3">
              Ingresar
            </button>
          </div>
        </form>
        <div className="mt-4">
          <Link to="/registro" className="text-muted small">
            Nuevo usuario
          </Link>
          <br />
          <Link to="/recuperar-contrasena" className="text-muted small">
            He olvidado mi contraseña
          </Link>
        </div>
      </div>
    </main>
  );
}

export default Login;
