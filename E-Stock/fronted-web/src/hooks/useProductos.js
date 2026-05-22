import { useState, useEffect } from "react";
import { getProductos, deleteProducto } from "../services/productosService";

export const useProductos = () => {
  const [productos, setProductos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchProductos = async () => {
    try {
      setLoading(true);
      const data = await getProductos();
      setProductos(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProductos();
  }, []);

  const removeProducto = async (id) => {
    if (!window.confirm("¿Seguro que quieres borrar este producto?")) return;
    try {
      await deleteProducto(id);
    
      setProductos((prev) => prev.filter((p) => p.id !== id));
    } catch (err) {
      alert(err.message);
    }
  };

  return { productos, loading, error, removeProducto };
};