import { useState } from "react";
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Package, Home, ReceiptText, Boxes, BarChart3, User, LogOut, LogIn } from "lucide-react";

export default function DashboardLayout() {
  const [searchTerm, setSearchTerm] = useState("");
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const isActive = (path) => location.pathname === path ? "active" : "";

  return (
    <div className="dashboard-container">
      
      <aside className="sidebar">
        <div className="logo-section" style={{ display: 'flex', alignItems: 'center', gap: '10px', justifyContent: 'center' }}>
          <Package size={28} />
          <span>E-Stock</span>
        </div>
        <nav>
          <ul>
            <li>
              <Link to="/" className={isActive("/")} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Home size={20} /> Inicio
              </Link>
            </li>
            {user && (
              <>
                <li>
                  <Link to="/facturacion" className={isActive("/facturacion")} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <ReceiptText size={20} /> Facturación
                  </Link>
                </li>
                <li>
                  <Link to="/inventario" className={isActive("/inventario")} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <Boxes size={20} /> Inventario
                  </Link>
                </li>
                <li>
                  <Link to="/reportes" className={isActive("/reportes")} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <BarChart3 size={20} /> Reportes
                  </Link>
                </li>
              </>
            )}
          </ul>
        </nav>
      </aside>

      {/* --- ÁREA PRINCIPAL --- */}
      <main className="main-wrapper">
       
        <header className="top-header">
          <div className="search-container">
            <input 
              type="text" 
              placeholder="Buscar..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <div className="header-actions">
            {user ? (
              <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                <span style={{ fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '5px' }}>
                  <User size={18} /> {user.nombre}
                </span>
                <button 
                  onClick={() => { logout(); navigate('/login'); }}
                  style={{ background: '#e74c3c', padding: '8px 15px', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '5px' }}
                >
                  <LogOut size={16} /> Cerrar sesión
                </button>
              </div>
            ) : (
              <div style={{ display: 'flex', gap: '10px' }}>
                <button 
                  onClick={() => navigate('/login')}
                  style={{ background: '#2980b9', padding: '8px 15px', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '5px' }}
                >
                  <LogIn size={16} /> Iniciar sesión / Registrar
                </button>
              </div>
            )}
          </div>
        </header>

       
        <div className="content-area">
          <Outlet context={{ searchTerm }} />
        </div>
      </main>
    </div>
  );
}