import React, { useState, useEffect } from "react";
import { useAuth } from "../utils/AuthContext.jsx";
import { useNavigate, Link } from "react-router-dom";
import Layout from "../components/Layout";
import "./Home.css"; // Importa los estilos CSS

const Home = () => {
  return (
    <Layout>
      <section className="content-placeholder bg-placeholder-grey rounded-4 align-self-center flex-grow-1 mb-5"></section>
    </Layout>
  );
};

export default Home;
