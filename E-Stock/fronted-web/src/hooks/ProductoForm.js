import React, { useEffect, useState } from "react";
import { useCreateProducto } from "./useCreateProducto";

const ProductoForm = () => {
  const { addProducto, fetchProductos, productos, loading, error } = useCreateProducto();
  const [formData, setFormData] = useState({ nombre: "", precio: "" });
  const [validationErrors, setValidationErrors] = useState({});

 
  useEffect(() => {
    fetchProductos();
  }, [fetchProductos]);


  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

 
  const validateForm = () => {
    const errors = {};
    if (!formData.nombre) {
      errors.nombre = "El nombre es obligatorio.";
    }
    if (!formData.precio) {
      errors.precio = "El precio es obligatorio.";
    } else if (isNaN(formData.precio) || formData.precio <= 0) {
      errors.precio = "El precio debe ser un número positivo.";
    }
    return errors;
  };

 
  const handleSubmit = async (e) => {
    e.preventDefault();
    const errors = validateForm();
    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      return;
    }
    setValidationErrors({});
    const success = await addProducto(formData);
    if (success) {
      setFormData({ nombre: "", precio: "" }); // Limpiar el formulario
    }
  };

  return (
    <div>
      <h1>Gestión de Productos</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Nombre:</label>
          <input
            type="text"
            name="nombre"
            value={formData.nombre}
            onChange={handleChange}
          />
          {validationErrors.nombre && (
            <p style={{ color: "red" }}>{validationErrors.nombre}</p>
          )}
        </div>
        <div>
          <label>Precio:</label>
          <input
            type="text"
            name="precio"
            value={formData.precio}
            onChange={handleChange}
          />
          {validationErrors.precio && (
            <p style={{ color: "red" }}>{validationErrors.precio}</p>
          )}
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Guardando..." : "Agregar Producto"}
        </button>
      </form>
      {error && <p style={{ color: "red" }}>Error: {error}</p>}
      <h2>Lista de Productos</h2>
      <ul>
        {productos.map((producto) => (
          <li key={producto._id}>
            {producto.nombre} - ${producto.precio}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProductoForm;