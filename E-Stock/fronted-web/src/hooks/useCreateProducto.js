import { useState } from "react";
import { createProducto, getProductos } from "../services/productosService";

export const useCreateProducto = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [productos, setProductos] = useState([]);

  // Función para agregar un producto
  const addProducto = async (data) => {
    try {
      setLoading(true);
      setError(null);
      await createProducto(data);
      await fetchProductos(); 
      return true;
    } catch (err) {
      setError(err.message);
      return false;
    } finally {
      setLoading(false);
    }
  };

  // Función para obtener productos
  const fetchProductos = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getProductos();
      setProductos(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return { addProducto, fetchProductos, productos, loading, error };
};