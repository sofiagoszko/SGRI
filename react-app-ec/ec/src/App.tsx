import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Registro from "./pages/Registro";
import RecuperarPass from "./pages/RecuperarPass";
import NuevoRequerimiento from "./pages/Requerimiento/Nuevo";
import MisAsignaciones from "./pages/Requerimiento/MisAsignaciones";
import MisSolicitudes from "./pages/Requerimiento/MisSolicitudes";
import Solicitudes from "./pages/Requerimiento/Solicitudes";
import { AuthProvider } from "./utils/AuthContext";
function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/registro" element={<Registro />} />
          <Route path="/recuperar-contrasena" element={<RecuperarPass />} />
          <Route path="/home" element={<Home />} />
          <Route path="/requerimiento/nuevo" element={<NuevoRequerimiento />} />
          <Route
            path="/requerimiento/mis-asignaciones"
            element={<MisAsignaciones />}
          />
          <Route
            path="/requerimiento/mis-solicitudes"
            element={<MisSolicitudes />}
          />
          <Route path="/requerimiento/solicitudes" element={<Solicitudes />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
