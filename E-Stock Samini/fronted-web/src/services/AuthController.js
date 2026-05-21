import axios from "axios";

const API_URL = "http://localhost:8081/api/v1/auth";

export const login = async (nombre, password) => {
  try {
   
    const res = await axios.post(`${API_URL}/login`, { nombre, password });
    
    
    if (res.data) {
        localStorage.setItem("user", JSON.stringify(res.data));
    }
    return res.data;
  } catch (error) {
    throw new Error("Credenciales inválidas");
  }
};

export const register = async (usuarioData) => {
  try {
    const res = await axios.post(`${API_URL}/register`, usuarioData);
    return res.data;
  } catch (error) {
    throw new Error("Error en el registro");
  }
};

export const logout = () => {
  localStorage.removeItem("user");
};