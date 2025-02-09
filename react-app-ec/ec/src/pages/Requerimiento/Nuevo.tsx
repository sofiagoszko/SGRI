import React, { useState, useEffect } from "react";
import { useAuth } from "../../utils/AuthContext.jsx";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout";
import "./Nuevo.css"; // Importa los estilos CSS

const Nuevo = () => {
  const navigate = useNavigate();
  const handleSubmit = (e: any) => {
    e.preventDefault();
    console.log("casa");
    navigate("/home");
  };
  return (
    <Layout>
      <section className="content-placeholder bg-white rounded-4 align-self-center flex-grow-1 mb-5">
        <h1 className="text-center mt-3">Alta nuevo requerimiento</h1>
        <form
          onSubmit={handleSubmit}
          className="row container mt-4 row-gap-4 mx-auto justify-content-center"
        >
          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="categoria">
                Categoría <span className="text-danger">*</span>
              </label>
              <select className="form-control" id="categoria" required>
                <option value="" selected hidden>
                  Categoría
                </option>
                <option>Hardware</option>
                <option>Software</option>
                <option>Red</option>
                <option>Seguridad</option>
              </select>
            </div>
          </div>

          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="prioridad">
                Prioridad <span className="text-danger">*</span>
              </label>
              <select
                className="form-control"
                id="prioridad"
                name="prioridad"
                required
              >
                <option value="" selected hidden>
                  Prioridad
                </option>
                <option>Baja</option>
                <option>Media</option>
                <option>Alta</option>
                <option>Urgente</option>
              </select>
            </div>
          </div>

          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="tipo">
                Tipo <span className="text-danger">*</span>
              </label>
              <select className="form-control" id="tipo" name="tipo" required>
                <option value="" selected hidden>
                  Tipo
                </option>
                <option value="REH">REH</option>
                <option value="ERR">ERR</option>
                <option value="GOP">GOP</option>
              </select>
            </div>
          </div>

          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="destinatario">Destinatario</label>
              <select className="form-control" id="destinatario">
                <option value="" selected hidden>
                  Destinatario
                </option>
                <option>Silvia Romero</option>
                <option>Aldo Fleitas</option>
                <option>Sofia Goszko</option>
                <option>Sebastian Merlino</option>
              </select>
            </div>
          </div>

          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="asunto">
                Asunto <span className="text-danger">*</span>
              </label>
              <input
                type="text"
                className="form-control"
                id="asunto"
                placeholder="Asunto"
                required
              />
            </div>
          </div>

          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="requerimiento">Requerimiento Relacionado</label>
              <select
                className="form-control"
                id="requerimiento"
                name="requerimiento"
              >
                {" "}
                <option value="" selected hidden>
                  Requerimiento
                </option>
                <option>REH-2024-0000000001</option>
                <option>ERR-2022-0000000002</option>
                <option>GOP-2024-0000000003</option>
              </select>
            </div>
          </div>

          <div className="col-10">
            <div className="form-group">
              <label htmlFor="prioridad">Descripción</label>
              <div className="form-floating">
                <textarea
                  className="form-control textarea-lg"
                  placeholder="Leave a comment here"
                  id="floatingTextarea"
                ></textarea>
                <label htmlFor="floatingTextarea">Descripción</label>
              </div>
            </div>
          </div>

          <div className="col-12"></div>

          <div className="col-12 col-md-5 mx-auto">
            <div id="drop-area">
              <h2 className="m-0">Arrastra uno o más archivos</h2>
              <p className="m-0">
                <i className="bi bi-cloud-upload bi-lg"></i>
              </p>
              <input type="file" id="fileElem" multiple />
              <label
                htmlFor="fileElem"
                id="upload-btn"
                className="btn fw-semibold"
              >
                Buscar Archivos
              </label>
              <ul id="fileList"></ul>
            </div>
          </div>

          <div className="d-flex justify-content-around mb-5">
            <Link to="/home" className="btn btn-secondary">
              Cancelar
            </Link>
            <button type="submit" className="btn btn-success">
              Aceptar
            </button>
          </div>
        </form>
      </section>
    </Layout>
  );
};

export default Nuevo;
