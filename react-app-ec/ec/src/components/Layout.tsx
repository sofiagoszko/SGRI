import React from "react";
import { useAuth } from "../utils/AuthContext.jsx";
import { useLocation } from "react-router";
import { NavLink } from "react-router-dom";
import "./Layout.css";
import imagenPerfil from "../SERGI_fondo_blanco.jpeg";
import { ToastContainer } from 'react-toastify';

function Layout({ children }) {
  return (
    <>
      <header className="layout__header mx-4 my-3 gap-3">
        <UserCard />
        <NavCard />
      </header>
      <main className="d-flex flex-column flex-grow-1">{children}
      <ToastContainer aria-label="toast"/>
      </main>
    </>
  );
}

// const user = localStorage.getItem('user');
// const userId = localStorage.getItem('userId');

const UserCard = () => {
  const { handleLogout } = useAuth();
  const user = localStorage.getItem("user");
  const userId = localStorage.getItem("userId");
  // Redirigir al login cuando se hace logout

  return (
    <div className="card card__user d-flex flex-column align-items-center rounded-bottom-4 shadow-sm border-0">
      <img
        src={imagenPerfil}
        alt="Imagen de perfil"
        className="card__user-foto-perfil rounded-circle mt-3 border"
      />
      <p className="small mb-4">{user}</p>
      <div className="d-flex gap-3 mb-2">
        <NavLink to={`/usuario/${userId}`} className="text-decoration-none text-black">
          Mi cuenta
        </NavLink>
        {" "}
        |{" "}
        <button
          onClick={handleLogout} // Llama a la función de logout desde el contexto
          className="text-decoration-none text-black bg-transparent border-0"
        >
          Cerrar sesión
        </button>
      </div>
    </div>
  );
};

const baseButtonStyle = "btn btn__menu d-flex flex-column";

const siLaRutaEs = (route: string) => {
  let location = useLocation();
  return location.pathname === route;
};


const NavCard = () => (
  <nav className="menu h-100 rounded-bottom-4 shadow-sm align-items-end">
    <NavLink
      to="/requerimiento/nuevo"
      className={`${baseButtonStyle} ${
        siLaRutaEs("/requerimiento/nuevo") ? "active" : ""
      }`}
    >
      <i className="bi bi-ticket-perforated bi-lg"></i>
      Nuevo requerimiento
    </NavLink>
    <NavLink
      to="/requerimiento/mis-asignaciones"
      className={`${baseButtonStyle} ${
        siLaRutaEs("/requerimiento/mis-asignaciones") ? "active" : ""
      }`}
    >
      <i className="bi bi-list-ul bi-lg"></i>
      Mis Asignaciones
    </NavLink>
    <NavLink
      to="/requerimiento/mis-solicitudes"
      className={`${baseButtonStyle} ${
        siLaRutaEs("/requerimiento/mis-solicitudes") ? "active" : ""
      }`}
    >
      <i className="bi bi-pencil-square bi-lg"></i>
      Mis Solicitudes
    </NavLink>
    <NavLink
      to="/requerimiento/solicitudes"
      className={`${baseButtonStyle} ${
        siLaRutaEs("/requerimiento/solicitudes") ? "active" : ""
      }`}
    >
      <i className="bi bi-search bi-lg"></i>
      Explorar Solicitudes
    </NavLink>
  </nav>
);

export default Layout;
