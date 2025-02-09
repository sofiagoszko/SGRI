import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./RecuperarPass.css";
import getBaseUrl from "../utils/getBaseUrl";

function Login() {
  // Estado para los campos del formulario y el error
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const navigate = useNavigate();
  const endpoint = getBaseUrl();

  const AUTH_ENDPOINT = "/api/auth/credentials"; // Constante para la URL de autenticación

  // Maneja el envío del formulario
  const handleSubmit = async (e) => {
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
    <main className="main__recuperar-pass">
      <div className="bg-white p-5 rounded-5 card__recuperar-pass">
        <h1 className="mb-4 text-center fw-normal">Recuperar contraseña</h1>
        <form onSubmit={handleSubmit} className="d-flex flex-column">
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Correo electrónico
            </label>
            <input type="email" className="form-control rounded-3" id="email" />
          </div>
          <div className="d-flex justify-content-evenly gap-3">
            <Link
              to="/"
              className="btn btn-danger text-white align-self-center px-5 mt-3"
            >
              Cancelar
            </Link>
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
