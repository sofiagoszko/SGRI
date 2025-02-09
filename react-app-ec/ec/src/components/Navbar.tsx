import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/js/dist/collapse';
import 'bootstrap/js/dist/offcanvas';
import { useNavigate, Link } from 'react-router-dom';
import logo from '../Logo-ec.png';
import logo2 from '../Logo2-ec.png';
import { useAuth } from '../utils/AuthContext';
function Navbar() {
    const navigate = useNavigate();
    const { isAuthenticated, setIsAuthenticated } = useAuth();
    const handleLogout = () => {
        localStorage.setItem('professorId', '');
        setIsAuthenticated(false)
        navigate('/'); 
    };
    return (
      <nav style={{
          width: '100%',
          backgroundColor: '#1A525B',
      }} className="navbar navbar-expand-lg">
          <div className="container-fluid">
            <div>
                <img style={{ width: '50px', height: '50px' }} src={logo2} alt="Logo2" />
                      <Link className="navbar-brand fw-bold" to="/" style={{ color: '#fff' }}>Easy Choice</Link>
            </div>
            <button className="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasDarkNavbar" aria-controls="offcanvasDarkNavbar">
              <span className="navbar-toggler-icon"></span>
            </button>
                  <div className="offcanvas offcanvas-end" style={{
                      backgroundColor: '#1A525B',
                      color: "white"
                  }} tabIndex="-1" id="offcanvasDarkNavbar" aria-labelledby="offcanvasDarkNavbarLabel" >
              <div className="offcanvas-header">
                <img style={{ width: '100px', height: '100px' }} src={logo} alt="Logo" />
                <button type="button" className="btn-close btn-close-white" data-bs-dismiss="offcanvas" aria-label="Close"></button>
              </div>
              <div className="offcanvas-body" >
                <ul className="navbar-nav justify-content-end flex-grow-1 pe-3" >
                              {isAuthenticated ? (
                                    <>
                                        <li className="nav-item"><Link className="nav-link" to="/mis-examenes" style={{ color: 'white' }}>Mis Examenes</Link></li>
                                        <li className="nav-item"><button className="nav-link" onClick={handleLogout} style={{ color: 'white', background: 'none', border: 'none' }}>Logout</button></li>
                                    </>
                                ) : (
                                    <>
                                        <li className="nav-item"><Link className="nav-link" to="/registrarse" style={{ color: 'white' }}>Registrarse</Link></li>
                                        <li className="nav-item"><Link className="nav-link" to="/login" style={{ color: 'white' }}>Login</Link></li>
                                    </>
                                )}
                </ul>
              </div>
            </div>
          </div>
        </nav>
    );
}

export default Navbar;