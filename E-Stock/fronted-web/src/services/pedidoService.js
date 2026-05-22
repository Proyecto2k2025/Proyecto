import axios from "axios";

const API_URL = "http://localhost:8081/api/v1/pedidos";

export const getPedidosPorFecha = async (fecha) => {
  try {
    
    const res = await axios.get(`${API_URL}/fecha/${fecha}`);
    if (!res.data || !Array.isArray(res.data)) return [];
    return res.data;
  } catch (error) {
    console.error("Error al obtener pedidos:", error);
    return [];
  }
};

export const crearPedido = async (pedido) => {
  try {
    const res = await axios.post(API_URL, pedido);
    return res.data;
  } catch (error) {
    console.error("Error al crear pedido:", error);
    throw error;
  }
};


export const actualizarEstadoPedido = async (id, nuevoEstado) => {
  try {
    
    const res = await axios.put(`${API_URL}/${id}/estado`, null, {
      params: { estado: nuevoEstado }
    });
    return res.data;
  } catch (error) {
    console.error("Error al actualizar estado:", error);
    throw error;
  }
};