import { useState, useEffect, createContext, useContext } from 'react';

const AuthContext = createContext(); // Crear contexto

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem('authToken') !== null;
    });
    useEffect(() => {
        console.log("isAuthenticated:", isAuthenticated);  // Monitorea cuando se actualiza el estado
    }, [isAuthenticated]);

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    console.log("AuthContext values:", context);
    return context;
};