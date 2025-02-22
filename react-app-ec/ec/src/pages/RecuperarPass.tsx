import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./RecuperarPass.css";
import Swal from "sweetalert2";
//import getBaseUrl from "../utils/getBaseUrl";

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
    //navigate('/')

    const url = `http://localhost:8080/api/usuario-empresa/usuarios/email/${email}`
    console.log("URL: ", url)
    try {
        const response = await fetch(url, {
            method: 'GET',
        });

        const  data = await response.json();
        console.log("Respuesta del backend:", data);
        console.log(response.ok);

        if (response.ok) {
          Swal.fire({
            icon: "success",
            title: "Éxito",
            text: "Se ha enviado un correo para recuperar tu contraseña",
          }).then(() => {
            navigate("/");
          });
        }else{ 
            console.error(data?.userFriendlyMessage || "No se encontró un usuario con ese correo");
            Swal.fire({
              icon: "error",
              title: "Error",
              text: "No se encontró un usuario con ese correo",
            });
        }
    } catch (error) {
      console.error("Error en la recuperación de contraseña:", error);
      setAlertMessage(`Error: ${error.message}`);
      setAlertType("danger");
      setShowAlert(true);
      console.error(error); 
    }
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
