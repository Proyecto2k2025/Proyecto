import axios from "axios";


const API_URL_REPORTES = "http://localhost:8081/api/v1/reportes";
const API_URL_PEDIDOS = "http://localhost:8081/api/v1/pedidos";


const obtenerFechaLocal = () => {
    const ahora = new Date();
    const anio = ahora.getFullYear();
    const mes = String(ahora.getMonth() + 1).padStart(2, '0');
    const dia = String(ahora.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
};


export const getResumenDiario = async (fecha = null) => {
  try {
    const dateQuery = fecha || obtenerFechaLocal();
    const res = await axios.get(`${API_URL_PEDIDOS}/resumen/${dateQuery}`);
    return res.data;
  } catch (error) {
    console.error("Error al obtener resumen diario:", error);
    
    return {
      ventasTotales: 0,
      dineroTotalVentas: 0,
      gastosTotales: 0,
      dineroTotalGastos: 0,
      balanceDia: 0
    };
  }
};


export const getAnnualSalesData = async () => {
  try {
    const res = await axios.get(`${API_URL_REPORTES}/ventas/anual`);
   
    if (Array.isArray(res.data)) {
        
        const meses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
        return res.data.map(item => ({
            mes: meses[item.mes - 1] || `Mes ${item.mes}`,
            ventas: item.ventas
        }));
    }
    return [];
  } catch (error) {
    console.error("Error al obtener ventas anuales:", error);
    return [];
  }
};
