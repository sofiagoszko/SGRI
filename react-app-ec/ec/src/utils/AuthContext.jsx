import { useState, useEffect, createContext, useContext } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext(); // Crear contexto

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem('authToken') !== null;
    });
    useEffect(() => {
        console.log("isAuthenticated:", isAuthenticated);  // Monitorea cuando se actualiza el estado
    }, [isAuthenticated]);


    const navigate = useNavigate();  

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        localStorage.removeItem('userId');
        setIsAuthenticated(false);
        navigate("/"); 
    }    

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};