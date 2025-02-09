import React from "react";
import { useLocation } from "react-router";
import { NavLink } from "react-router-dom";
import "./Layout.css";

function Layout({ children }) {
  return (
    <>
      <header className="layout__header mx-4 my-3 gap-3">
        <UserCard />
        <NavCard />
      </header>
      <main className="d-flex flex-column flex-grow-1">{children}</main>
    </>
  );
}

const UserCard = () => (
  <div className="card card__user d-flex flex-column align-items-center rounded-bottom-4 shadow-sm border-0">
    <img
      src="https://attic.sh/dffv7kof6q1a62mama4i2x05pmyr"
      alt="Imagen de perfil"
      className="card__user-foto-perfil rounded-circle mt-3 border"
    />
    <p className="small mb-4">admin</p>
    <div className="d-flex gap-3 mb-2">
      {/* <a href="#" className="text-decoration-none text-black">
        Mi cuenta
      </a>{" "}
      |{" "} */}
      <NavLink to="/Home" className="text-decoration-none text-black">
        Mi cuenta
      </NavLink>
      {" "}
      |{" "}
      <NavLink to="/" className="text-decoration-none text-black">
        Cerrar sesión
      </NavLink>
    </div>
  </div>
);

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
