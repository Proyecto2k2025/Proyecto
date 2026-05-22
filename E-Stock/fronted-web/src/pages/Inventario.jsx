import { useEffect, useState } from "react";
import { getProductos, deleteProducto } from "../services/productosService";
import { Link, useOutletContext } from "react-router-dom";
import { PackagePlus } from "lucide-react";

export default function Inventario() {
  const [productos, setProductos] = useState([]);

  // Cargar datos al iniciar
  useEffect(() => {
    cargar();
  }, []);

  const cargar = async () => {
    const data = await getProductos();
    setProductos(data);
  };

  const eliminar = async (id) => {
    if (window.confirm("¿Seguro que deseas eliminar este producto?")) {
      try {
        await deleteProducto(id);
        cargar(); // Recargar la lista
      } catch (error) {
        alert("No se pudo eliminar. Verifique si tiene ventas asociadas.");
      }
    }
  };

  const { searchTerm } = useOutletContext() || { searchTerm: "" };

  const productosFiltrados = productos.filter(p => 
    p.nombre?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.descripcion?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.categoria?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.id?.toString().includes(searchTerm)
  );

  return (
    <>
      <div className="page-header-card">
        <h2 className="page-title">Inventario actual</h2>
      </div>

      <div className="card">
        <table className="table-container">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre del producto</th>
              <th>Descripción</th>
              <th>Precio</th>
              <th>Categoría</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {productosFiltrados.length > 0 ? (
              productosFiltrados.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.nombre}</td>
                  <td>{p.descripcion}</td>
                  <td className="text-green">€ {p.precio}</td>
                  <td>{p.categoria}</td>
                  <td>
                    <button 
                      onClick={() => eliminar(p.id)} 
                      className="text-red" 
                      style={{border:'none', background:'none', cursor:'pointer', fontWeight:'bold'}}
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" style={{textAlign:'center', padding:'20px'}}>
                  {searchTerm ? "No se encontraron productos para tu búsqueda." : "No hay productos registrados."}
                </td>
              </tr>
            )}
          </tbody>
        </table>

        <div className="floating-actions">
          <Link to="/ingreso-producto" className="btn btn-action" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <PackagePlus size={20} /> Ingresar productos
          </Link>
        </div>
      </div>
    </>
  );
}