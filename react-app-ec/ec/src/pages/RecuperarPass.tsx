import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./RecuperarPass.css";
import getBaseUrl from "../utils/getBaseUrl";

function recuperacion() {
  const [email, setEmail] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const navigate = useNavigate();

  // Maneja el envío del formulario
  const handleSubmit = async (e:any) => {
    e.preventDefault();
    setShowAlert(false);
    navigate('/')
    // try {
    //     const response = await fetch(`http://localhost:8080/api/usuario-empresa/usuarios/email/${encodeURIComponent(email)}`, {
    //         method: 'GET',

    //     });
    //       console.log("URL de la solicitud:", `http://localhost:8080/api/usuario-empresa/usuarios/email/${encodeURIComponent(email)}`);
    //       const data = await response.json();
    //       console.log("Respuesta del backend:", data);

    //     if (response.ok) {
    //         setAlertMessage('Se ha enviado un correo para recuperar tu contraseña');
    //         setAlertType("success");
    //         setShowAlert(true);
    //         //navigate('/');
    //     } else {
    //         setAlertMessage(data?.userFriendlyMessage || "No se encontró un usuario con ese correo");
    //         setAlertType('warning')
    //         setShowAlert(true); 
    //     }
    // } catch (error) {
    //   console.error("Error en la recuperación de contraseña:", error);
    //   setAlertMessage(`Error: ${error.message}`);
    //   setAlertType("danger");
    //   setShowAlert(true);
    //   console.error(error); 
    // }
  };

  return (
    <main className="main__recuperar-pass">
      <div className="bg-white p-5 rounded-5 card__recuperar-pass">
        <h1 className="mb-4 text-center fw-normal">Recuperar contraseña</h1>

        {showAlert && alertMessage && (
          <div className={`alert alert-${alertType}`} role="alert">
            {alertMessage}
          </div>
        )} 

        <form onSubmit={handleSubmit} className="d-flex flex-column">
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Correo electrónico
            </label>
            <input 
              type="email" 
              className="form-control rounded-3" 
              id="email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
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

export default recuperacion;
