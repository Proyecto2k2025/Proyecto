import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  
  const [isRegister, setIsRegister] = useState(false);
  const [nombre, setNombre] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const url = isRegister 
      ? 'http://localhost:8081/api/v1/auth/register' 
      : 'http://localhost:8081/api/v1/auth/login';

    const payload = {
      nombre,
      password
    };

    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Error en la autenticación');
      }

     
      if (isRegister) {
        const loginRes = await fetch('http://localhost:8081/api/v1/auth/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
        
        if (loginRes.ok) {
          const data = await loginRes.json();
          login(data);
          navigate('/');
          return;
        }
      }

      const data = await response.json();
      login(data); 
      navigate('/');
    } catch (err) {
      if (err.message === 'Failed to fetch') {
        setError('Error de conexión. Verifica que el backend esté ejecutándose correctamente.');
      } else {
        setError(err.message);
      }
    }
  };

  return (
    <div className="login-layout">
      <div className="login-left">
        <h1>📦 E-Stock</h1>
        <h3>{isRegister ? '¡Regístrate!' : '¡Bienvenido!'}</h3>
        <p>{isRegister ? 'Crea una cuenta para comenzar.' : 'Potencia tu negocio y obtén mejores resultados.'}</p>
        
        {error && <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}

        <form onSubmit={handleSubmit}>
          <input 
            className="login-input" 
            placeholder="Nombre de usuario" 
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
          <input 
            className="login-input" 
            placeholder="Contraseña" 
            type="password" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button className="btn" style={{width:'100%', marginTop:20, justifyContent:'center'}}>
            {isRegister ? '¡Registrarse!' : '¡Iniciar Sesión!'}
          </button>
        </form>
      </div>
      <div className="login-right">
        <div style={{textAlign:'center'}}>
           <h2>{isRegister ? '¿Ya estás registrado?' : '¿Aún no tienes cuenta?'}</h2>
           <button 
             className="btn" 
             onClick={() => setIsRegister(!isRegister)} 
             style={{background:'#2980b9', color:'white', margin:'10px auto'}}
           >
             {isRegister ? 'Iniciar sesión' : 'Registrarse'}
           </button>
        </div>
      </div>
    </div>
  );
}