import { useEffect, useState } from "react";
import { useOutletContext } from "react-router-dom";
import axios from "axios";
import { getPedidosPorFecha, crearPedido, actualizarEstadoPedido } from "../services/pedidoService";
import { getProductos } from "../services/productosService";
import { CheckCircle, Clock, Printer, Check, Plus, ShoppingCart } from "lucide-react";

const obtenerFechaLocal = () => {
    const ahora = new Date();
    const anio = ahora.getFullYear();
    const mes = String(ahora.getMonth() + 1).padStart(2, '0');
    const dia = String(ahora.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
};

function ModalNuevaFactura({ onClose, onGuardar }) {
  const [productos, setProductos] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [carrito, setCarrito] = useState([]);
  const [seleccionId, setSeleccionId] = useState("");
  const [cantidad, setCantidad] = useState(1);
  const [clienteId, setClienteId] = useState("");
  const [estadoPedido, setEstadoPedido] = useState("ENTREGADO"); // Nuevo estado para controlar
  
  const [mostrarCrearCliente, setMostrarCrearCliente] = useState(false);
  const [nuevoCliente, setNuevoCliente] = useState({nombre: '', email: '', telefono: '', direccion: ''});

  useEffect(() => {
    getProductos().then(data => { if (Array.isArray(data)) setProductos(data); });
    cargarClientes();
  }, []);

  const cargarClientes = async () => {
    try {
        const res = await axios.get("http://localhost:8081/api/v1/clientes");
        if (Array.isArray(res.data)) setClientes(res.data);
    } catch (e) {
        console.error("Error al cargar clientes", e);
    }
  };

  const crearClienteRapido = async () => {
      try {
          const res = await axios.post("http://localhost:8081/api/v1/clientes", nuevoCliente);
          setClientes([...clientes, res.data]);
          setClienteId(res.data.id);
          setMostrarCrearCliente(false);
      } catch (e) {
          alert("Error al crear cliente.");
      }
  };

  const agregarAlCarrito = () => {
    const prod = productos.find(p => p.id == seleccionId);
    if (!prod) return;
    
    const varianteId = (prod.variantes && prod.variantes.length > 0) ? prod.variantes[0].id : prod.id; 

    const existe = carrito.find(item => item.id === prod.id);
    if (existe) {
        setCarrito(carrito.map(item => 
            item.id === prod.id 
            ? { ...item, cantidad: item.cantidad + parseInt(cantidad), subtotal: item.precio * (item.cantidad + parseInt(cantidad)) }
            : item
        ));
    } else {
        setCarrito([...carrito, {
          id: prod.id, nombre: prod.nombre, precio: prod.precio,
          cantidad: parseInt(cantidad), subtotal: prod.precio * parseInt(cantidad),
          varianteIdReal: varianteId 
        }]);
    }
  };

  const confirmarVenta = async () => {
    if (carrito.length === 0) return alert("El carrito está vacío");
    if (!clienteId) return alert("Debes seleccionar un cliente");

    const nuevoPedido = {
      fechaPedido: obtenerFechaLocal(), 
      estado: estadoPedido,
      total: carrito.reduce((acc, item) => acc + item.subtotal, 0),
      items: carrito.map(item => ({
        variante: { id: item.varianteIdReal }, 
        cantidad: item.cantidad,
        precioUnitario: item.precio
      })),
      cliente: { id: parseInt(clienteId) } 
    };

    try {
      await crearPedido(nuevoPedido);
      alert("¡Venta registrada con éxito!");
      onGuardar(); 
      onClose();   
    } catch (error) {
      if (error.response && error.response.status === 409) {
          alert("Stock insuficiente.");
      } else {
          alert("Error al guardar venta.");
      }
    }
  };

  return (
    <div style={{position:'fixed', top:0, left:0, width:'100%', height:'100%', background:'rgba(0,0,0,0.6)', display:'flex', justifyContent:'center', alignItems:'center', zIndex:1000}}>
      <div className="card" style={{width:'600px', padding:'30px', maxHeight:'90vh', overflowY:'auto'}}>
        <h3 className="page-title" style={{fontSize:'1.5rem'}}>Nueva Venta / Factura</h3>
        
        <div style={{display: 'flex', gap: '15px'}}>
            <div className="form-group" style={{background: '#f1f5f9', padding: '15px', borderRadius: '8px', flex: 2}}>
            <label style={{fontWeight: 'bold'}}>Cliente</label>
            <div style={{display: 'flex', gap: '20px'}}>
                <select className="form-input" style={{flex: 1}} value={clienteId} onChange={e => setClienteId(e.target.value)}>
                    <option value="">Seleccione un cliente...</option>
                    {clientes.map(c => (
                        <option key={c.id} value={c.id}>{c.nombre} ({c.email})</option>
                    ))}
                </select>
                <button className="btn" style={{background: '#2ecc71', color: 'white', padding: '10px 15px', display: 'flex', alignItems: 'center', gap: '5px'}} onClick={() => setMostrarCrearCliente(!mostrarCrearCliente)}>
                    <Plus size={16} /> Nuevo
                </button>
            </div>
            
            {mostrarCrearCliente && (
                <div style={{marginTop: '15px', display: 'grid', gap: '10px'}}>
                    <input className="form-input" placeholder="Nombre" onChange={e => setNuevoCliente({...nuevoCliente, nombre: e.target.value})} />
                    <input className="form-input" placeholder="Email" onChange={e => setNuevoCliente({...nuevoCliente, email: e.target.value})} />
                    <input className="form-input" placeholder="Teléfono" onChange={e => setNuevoCliente({...nuevoCliente, telefono: e.target.value})} />
                    <input className="form-input" placeholder="Dirección" onChange={e => setNuevoCliente({...nuevoCliente, direccion: e.target.value})} />
                    <button className="btn" style={{background: '#3498db', color: 'white', padding: '10px'}} onClick={crearClienteRapido}>Guardar Cliente</button>
                </div>
            )}
            </div>

            <div className="form-group" style={{background: '#fff3cd', padding: '15px', borderRadius: '8px', flex: 1}}>
                <label style={{fontWeight: 'bold', color: '#856404'}}>Estado</label>
                <select className="form-input" style={{marginTop: '5px'}} value={estadoPedido} onChange={e => setEstadoPedido(e.target.value)}>
                    <option value="ENTREGADO">Entregado</option>
                    <option value="PENDIENTE">Pendiente</option>
                </select>
            </div>
        </div>

        <div style={{display: 'flex', gap: '20px', marginTop: '20px'}}>
            <div className="form-group" style={{flex: 2}}>
            <label>Producto</label>
            <select className="form-input" onChange={e => setSeleccionId(e.target.value)}>
                <option value="">Seleccione...</option>
                {productos.map(p => (
                <option key={p.id} value={p.id}>{p.nombre} - €{p.precio}</option>
                ))}
            </select>
            </div>

            <div className="form-group" style={{flex: 1}}>
            <label>Cantidad</label>
            <input type="number" min="1" className="form-input" value={cantidad} onChange={e => setCantidad(e.target.value)} />
            </div>
        </div>

        <button onClick={agregarAlCarrito} className="btn" style={{width:'100%', marginBottom:'20px', marginTop: '10px', background: '#34495e', color: 'white', padding: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px'}}>
            <ShoppingCart size={18} /> Agregar al pedido
        </button>

        <div style={{background:'#f9f9f9', padding:'15px', borderRadius:'8px', marginBottom:'25px'}}>
          <h4 style={{margin: '0 0 10px 0'}}>Carrito de Venta</h4>
          {carrito.length === 0 && <p style={{color: '#999', margin: 0}}>Vacío</p>}
          {carrito.map((item, i) => (
            <div key={i} style={{display:'flex', justifyContent:'space-between', borderBottom:'1px solid #ddd', padding:'8px 0'}}>
              <span>{item.nombre} <span style={{color: '#7f8c8d'}}>x {item.cantidad}</span></span>
              <b>€{item.subtotal.toFixed(2)}</b>
            </div>
          ))}
          <div style={{textAlign:'right', marginTop:'15px', fontSize:'1.3em'}}>
            Total: <b style={{color: '#27ae60'}}>€{carrito.reduce((sum, item) => sum + item.subtotal, 0).toFixed(2)}</b>
          </div>
        </div>

        <div style={{display:'flex', gap:'25px', justifyContent:'flex-end'}}>
          <button onClick={onClose} className="btn" style={{background:'#e74c3c', color: 'white', padding: '12px 20px'}}>Cancelar</button>
          <button onClick={confirmarVenta} className="btn" style={{background:'#27ae60', color: 'white', padding: '12px 20px', display: 'flex', alignItems: 'center', gap: '8px'}}>
              <Printer size={18} /> Confirmar e Imprimir Factura
          </button>
        </div>
      </div>
    </div>
  );
}

export default function Facturacion() {
  const [pedidos, setPedidos] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const fechaHoy = obtenerFechaLocal();

  const cargarDatos = () => {
    getPedidosPorFecha(fechaHoy).then(data => {
      if (Array.isArray(data)) setPedidos(data);
      else setPedidos([]);
    });
  };

  useEffect(() => { cargarDatos(); }, []);

  const cambiarEstado = async (id, nuevoEstado) => {
    try {
      await actualizarEstadoPedido(id, nuevoEstado);
      cargarDatos(); // Recargar la lista para mostrar el cambio
    } catch (e) {
      alert("Error al cambiar de estado. ¿Agregaste el endpoint en Java?");
    }
  };

  const { searchTerm } = useOutletContext() || { searchTerm: "" };

  const pedidosFiltrados = pedidos.filter(p => 
    p.cliente?.nombre?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.estado?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.id?.toString().includes(searchTerm)
  );

  return (
    <>
      <div className="page-header-card" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
        <div>
            <h2 className="page-title" style={{marginBottom: '5px'}}>Facturación Diaria</h2>
            <p style={{color: '#7f8c8d', margin: 0}}>Ventas del día: <b>{fechaHoy}</b></p>
        </div>
        <button className="btn" style={{background: '#3498db', color: 'white', padding: '10px 20px', fontSize: '1.1em', display: 'flex', alignItems: 'center', gap: '8px'}} onClick={() => setShowModal(true)}>
            <Plus size={20} /> Nueva Factura
        </button>
      </div>

      <div className="card">
        <div className="data-row header" style={{background: '#f8f9fa', padding: '15px', borderRadius: '8px 8px 0 0', fontWeight: 'bold'}}>
          <span style={{flex:1}}>ID Factura</span>
          <span style={{flex:2}}>Cliente</span>
          <span style={{flex:2}}>Estado</span>
          <span style={{flex:1, textAlign:'right'}}>Total</span>
          <span style={{flex:1, textAlign:'center'}}>Acciones</span>
        </div>

        {pedidosFiltrados.length > 0 ? pedidosFiltrados.map((p) => (
          <div key={p.id} className="data-row" style={{padding: '15px', borderBottom: '1px solid #eee', display: 'flex', alignItems: 'center'}}>
            <span style={{flex:1}}><b>#{p.id.toString().padStart(4, '0')}</b></span>
            <span style={{flex:2}}>{p.cliente?.nombre || 'Consumidor'}</span>
            <span style={{flex:2}}>
                <span style={{display: 'inline-flex', alignItems: 'center', gap: '5px', background: p.estado === 'ENTREGADO' ? '#d4edda' : '#fff3cd', color: p.estado === 'ENTREGADO' ? '#155724' : '#856404', padding: '5px 10px', borderRadius: '20px', fontSize: '0.85em', fontWeight: 'bold'}}>
                    {p.estado === 'ENTREGADO' ? <CheckCircle size={14} /> : <Clock size={14} />}
                    {p.estado}
                </span>
            </span>
            <span style={{flex:1, textAlign:'right', fontSize: '1.1em', fontWeight: 'bold'}} className="text-green">
              €{p.total?.toFixed(2) || '0.00'}
            </span>
            <span style={{flex:1, display: 'flex', justifyContent: 'center', gap: '5px'}}>
                {p.estado === 'PENDIENTE' && (
                    <button 
                        onClick={() => cambiarEstado(p.id, 'ENTREGADO')}
                        style={{background: '#27ae60', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer', fontSize: '0.8em', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '5px'}}
                    >
                        <Check size={14} /> Finalizar
                    </button>
                )}
            </span>
          </div>
        )) : (
          <div style={{textAlign:'center', padding:'50px', color:'#777'}}>
              <h3 style={{color: '#bdc3c7'}}>{searchTerm ? "No se encontraron facturas para tu búsqueda." : "No hay ventas registradas hoy."}</h3>
              {!searchTerm && <p>Haz clic en "Nueva Factura" para registrar una venta.</p>}
          </div>
        )}

      </div>

      {showModal && <ModalNuevaFactura onClose={() => setShowModal(false)} onGuardar={cargarDatos} />}
    </>
  );
}