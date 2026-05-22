import { useState } from "react";
import { createProducto } from "../services/productosService";
import { useNavigate } from "react-router-dom";

export default function IngresoProducto() {
  const navigate = useNavigate();
  
  const [form, setForm] = useState({
    nombre: "",
    descripcion: "",
    precio: "",
    categoria: "Ropa", // Valor por defecto
    imagen: ""
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createProducto(form);
      alert("¡Producto guardado con éxito!");
      navigate("/inventario"); // Volver al inventario
    } catch (error) {
      alert("Error al guardar el producto.");
    }
  };

  return (
    <>
      <div className="page-header-card">
        <h2 className="page-title">Ingreso de productos</h2>
      </div>

      <div className="card" style={{ maxWidth: '800px', margin: '0 auto' }}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nombre del producto</label>
            <input 
              name="nombre" 
              className="form-input" 
              placeholder="Ej: Camiseta Polo" 
              onChange={handleChange} 
              required
            />
          </div>

          <div className="form-group">
            <label>Descripción</label>
            <input 
              name="descripcion" 
              className="form-input" 
              placeholder="Detalles..." 
              onChange={handleChange} 
            />
          </div>

          <div style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
            <div style={{ flex: 1 }}>
              <label className="form-group label" style={{display:'block', marginBottom:'5px', fontWeight:'bold'}}>Precio</label>
              <input 
                type="number" 
                name="precio" 
                className="form-input" 
                placeholder="0.00" 
                onChange={handleChange} 
                required
              />
            </div>
            <div style={{ flex: 1 }}>
              <label className="form-group label" style={{display:'block', marginBottom:'5px', fontWeight:'bold'}}>Categoría</label>
              <select name="categoria" className="form-input" onChange={handleChange}>
                <option value="Ropa">Ropa</option>
                <option value="Electronica">Electrónica</option>
                <option value="Hogar">Hogar</option>
                <option value="Alimentos">Alimentos</option>
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>URL Imagen</label>
            <input 
              name="imagen" 
              className="form-input" 
              placeholder="https://..." 
              onChange={handleChange} 
            />
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-primary-black" style={{ width: '100%' }}>
              Guardar producto
            </button>
          </div>
        </form>
      </div>
    </>
  );
}