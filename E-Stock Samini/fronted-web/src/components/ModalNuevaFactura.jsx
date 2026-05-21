import { useState, useEffect } from "react";
import { getProductos } from "../services/productosService"; 
import { crearPedido } from "../services/pedidoService";

export default function ModalNuevaFactura({ onClose, onGuardar }) {
  const [productos, setProductos] = useState([]);
  const [carrito, setCarrito] = useState([]);
  
  
  const [prodSeleccionado, setProdSeleccionado] = useState("");
  const [cantidad, setCantidad] = useState(1);
  const [metodoPago, setMetodoPago] = useState("Efectivo");

  useEffect(() => {
    getProductos().then(setProductos).catch(console.error);
  }, []);

  const agregarAlCarrito = () => {
    if (!prodSeleccionado) return;
    const producto = productos.find(p => p.id == prodSeleccionado);
    
    const nuevoItem = {
      producto: producto,
      cantidad: parseInt(cantidad),
      subtotal: producto.precio * parseInt(cantidad)
    };

    setCarrito([...carrito, nuevoItem]);
  };

  const guardarFactura = async () => {
    
    const nuevoPedido = {
      fecha: new Date().toISOString().split('T')[0],
      tipo: "Venta",
      metodoPago: metodoPago,
      total: carrito.reduce((sum, item) => sum + item.subtotal, 0),
     
      detalles: carrito.map(item => ({
          producto: { id: item.producto.id },
          cantidad: item.cantidad,
          precioUnitario: item.producto.precio
      }))
    };

    try {
      await crearPedido(nuevoPedido);
      alert("Factura guardada en MySQL!");
      onGuardar(); 
      onClose();  
    } catch (error) {
      alert("Error: " + error.message);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded shadow-lg w-96">
        <h2 className="text-xl font-bold mb-4">Nueva Venta</h2>

        {/* Seleccionar Producto */}
        <select 
          className="w-full border p-2 mb-2" 
          onChange={(e) => setProdSeleccionado(e.target.value)}
        >
          <option value="">Seleccione Producto...</option>
          {productos.map(p => (
            <option key={p.id} value={p.id}>{p.nombre} - ${p.precio}</option>
          ))}
        </select>

        {/* Cantidad */}
        <input 
          type="number" min="1" value={cantidad} 
          className="w-full border p-2 mb-2"
          onChange={(e) => setCantidad(e.target.value)} 
        />

        <button onClick={agregarAlCarrito} className="bg-blue-500 text-white w-full py-1 rounded mb-4">
          + Agregar Item
        </button>

        {/*Lista del Carrito */}
        <div className="bg-gray-100 p-2 mb-4 h-32 overflow-auto">
          {carrito.map((item, i) => (
            <div key={i} className="flex justify-between text-sm">
              <span>{item.producto.nombre} x{item.cantidad}</span>
              <span>${item.subtotal}</span>
            </div>
          ))}
        </div>

        {/* Total y Guardar */}
        <h3 className="text-right font-bold text-xl mb-4">
            Total: ${carrito.reduce((acc, item) => acc + item.subtotal, 0)}
        </h3>

        <div className="flex justify-end gap-2">
          <button onClick={onClose} className="bg-gray-300 px-4 py-2 rounded">Cancelar</button>
          <button onClick={guardarFactura} className="bg-green-600 text-white px-4 py-2 rounded">Terminar Venta</button>
        </div>
      </div>
    </div>
  );
}