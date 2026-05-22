import axios from "axios";


const API_URL = "http://localhost:8081/api/v1/productos";

export const getProductos = async () => {
  try {
    const res = await axios.get(API_URL);
    
    if (!res.data || !Array.isArray(res.data)) return [];
    return res.data;
  } catch (error) {
    console.error("Error al obtener productos:", error);
    return [];
  }
};

export const createProducto = async (producto) => {
  try {
    const res = await axios.post(API_URL, producto);
    return res.data;
  } catch (error) {
    console.error("Error al crear producto:", error);
    throw error;
  }
};

export const deleteProducto = async (id) => {
  try {
    await axios.delete(`${API_URL}/${id}`);
  } catch (error) {
    console.error("Error al eliminar producto:", error);
    throw error;
  }
};