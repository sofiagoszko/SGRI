import React, { useState, useEffect, FormEvent } from "react";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../../components/Layout";
import "./Nuevo.css"; // Importa los estilos CSS
import axios from "axios";
import {toast} from "react-toastify";
const authToken = localStorage.getItem("authToken") || "";

const Nuevo = () => {
  const navigate = useNavigate();
  const [tipos, setTipos] = useState([]);
  const [tipo, setTipo] = useState();
  const [categoriasSeleccionables, setCategoriasSeleccionables] = useState();
  const [usuarios, setUsuarios] = useState();
  const [destinatario, setDestinatario] = useState(0);
  const [prioridad, setPrioridad] = useState("");
  const [asunto, setAsunto] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [categoria, setCategoria] = useState();
  const [adjuntos, setAdjuntos] = useState<File[]>();

  useEffect(() => {
    if(authToken){
    axios.get(`${import.meta.env.VITE_API_URL}/tipo-requerimiento/tipos`, {
      headers: {
        Authorization: `Bearer ${authToken}`
      }
    }).then(res => {
      setTipos(res.data);
    })}
    axios.get(`${import.meta.env.VITE_API_URL}/usuario-empresa/usuarios`, {
      headers: {
        Authorization: `Bearer ${authToken}`
      }
    }).then(res => {
      setUsuarios(res.data.data);
    })
  }, [])
  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    /*{
    "estado": "Pendiente",
    "prioridad": "Media",
    "fechaHora": "2025-02-08T12:30:00",
    "asunto": "love",
    "descripcion": "emily",
    "codigo": "R",
    "categoriaTipo": "V",
    "tipoRequerimiento": 1,
    "usuarioEmisor": 1,
    "usuarioDestinatario": 2
} */
    const tipoSeleccionado = tipos.find((tipoSel) => tipoSel.id == tipo);
    console.log(tipoSeleccionado);
    const codigoGenerado = `${tipoSeleccionado?.codigo}_${new Date().getFullYear()}_${Math.floor(1000000000 + Math.random() * 9000000000)}`;
    const formData = new FormData();
    formData.append("datos", 
      new Blob(
        [JSON.stringify({
          estado: destinatario ? "Asignado" : "Abierto",
          prioridad,
          fechaHora: new Date().toISOString().split('.')[0],
          asunto,
          descripcion,
          codigo: codigoGenerado,
          categoriaTipo: categoria,
          tipoRequerimiento: tipo,
          usuarioEmisor: 1,
          usuarioDestinatario: parseInt(destinatario)
        })],
        {type: "application/json"}
      ));
    if(adjuntos)
      adjuntos.forEach((file, index) => {
        formData.append(`archivos[${index}]`, file);
      });

      fetch(`${import.meta.env.VITE_API_URL}/requerimiento/nuevo`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${authToken}`
        },
        body: formData
      }).then(res => {
        toast("Requerimiento creado con exito");
        setTimeout(() => navigate('/home'), 3000);
      })
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
              <label htmlFor="tipo">
                Tipo <span className="text-danger">*</span>
              </label>
              <select className="form-control" id="tipo" name="tipo" required onChange={(e) => {
                let tipoSeleccionado = tipos.find((tipo) => tipo.id == e.target.value);
                if(tipoSeleccionado){
                  console.log(tipoSeleccionado);
                setTipo(tipoSeleccionado?.id);
                setCategoriasSeleccionables(tipoSeleccionado?.categorias);
                }
                }}>
                <option value="" selected hidden>
                  Tipo
                </option>
                {tipos.map((tipo) => (
                  <option value={tipo?.id || ""}>
                    {tipo?.codigo || ""}
                  </option>
                  )
                )}
              </select>
            </div>
          </div>
          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="categoria">
                Categoría <span className="text-danger">*</span>
              </label>
              <select className="form-control" id="categoria" required disabled={!tipo} onChange={(e) => setCategoria(e.target.value)}>
                <option value="" selected hidden>
                  Categoría
                </option>
                {categoriasSeleccionables && categoriasSeleccionables.map((cat) => (
                  <option value={cat.descripcion}>
                  {cat.descripcion}
                </option>
                ))}
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
                onChange={(e) => setPrioridad(e.target.value)}
              >
                <option value="" selected hidden>
                  Prioridad
                </option>
                <option value="Baja">Baja</option>
                <option value="Media">Media</option>
                <option value="Alta">Alta</option>
                <option value="Urgente">Urgente</option>
              </select>
            </div>
          </div>

          

          <div className="col-12 col-md-5">
            <div className="form-group">
              <label htmlFor="destinatario">Destinatario</label>
              <select className="form-control" id="destinatario" onChange={(e) => setDestinatario(e.target.value)}>
                <option value="" selected hidden>
                  Destinatario
                </option>
                {usuarios && usuarios.map((user) => (
                  <option value={user.id}>
                  {`${user.nombre} ${user.apellido}`}
                </option>
                ))}
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
                onChange={(e) => setAsunto(e.target.value)}
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
                  onChange={(e) => setDescripcion(e.target.value)}
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
              <input type="file" id="fileElem" multiple onChange={(e) => {
                setAdjuntos(Array.from(e.target.files).slice(0, 5));
              }}/>
              <label
                htmlFor="fileElem"
                id="upload-btn"
                className="btn fw-semibold"
              >
                Buscar Archivos
              </label>  
            </div>
              <ul id="fileList">{adjuntos && adjuntos.map((file) => (<li>{file.name}</li>))}</ul>
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
