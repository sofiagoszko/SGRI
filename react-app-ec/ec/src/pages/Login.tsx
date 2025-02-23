import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./Login.css";
import { jwtDecode } from "jwt-decode";
import { Eye, EyeOff } from "lucide-react";

function Login() {
  // Estado para los campos del formulario y el error
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const navigate = useNavigate();

  // Maneja el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    //setShowAlert(true);
    // navigate("/home");
    try {
      //console.log(AUTH_ENDPOINT)
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/usuario-empresa/credenciales`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            userName: usuario,
            password: password,
          }),
        }
      );

      const data = await response.json();
      console.log("Respuesta del backend:", data);

      if (response.ok) {
        //const data = await response.json();
        const authToken = data.data;
        localStorage.setItem("authToken", authToken);
        localStorage.setItem("user", usuario);
        const decodedToken = jwtDecode(authToken) as { id: number };
        const userId = decodedToken.id;
        localStorage.setItem("userId", userId.toString());
        navigate("/home");
      } else {
        setAlertMessage(
          "Por favor, verifica tus credenciales y vuelve a intentarlo"
        );
        setAlertType("warning");
        setShowAlert(true);
      }
    } catch (error) {
      console.error("Error en la autenticación:", error);
      setAlertMessage(
        "Hubo un problema con el servidor. Inténtalo de nuevo más tarde."
      );
      setAlertType("danger");
      setShowAlert(true);
    }
  };

  return (
    <main className="main__login">
      <div className="bg-white p-5 rounded-5 card__login">
        <h1 className="mb-4 text-center">Iniciar sesión</h1>
        <form onSubmit={handleSubmit} className="d-flex flex-column">
          {showAlert && alertMessage && (
            <div className={`alert alert-${alertType}`} role="alert">
              {alertMessage}
            </div>
          )}

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
            <div className="input-group">
              <input
                type={showPassword ? "text" : "password"}
                className="form-control"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
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
