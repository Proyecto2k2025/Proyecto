import React from 'react';
import { Link } from 'react-router-dom';
import { BarChart3 } from 'lucide-react';

export default function Resumen() {
  return (
    <div>
      <div className="page-header-card">
        <h1 className="page-header-title">Resumen 14 de noviembre de 2023</h1>
      </div>

      <div className="card">
        <div className="data-row">
          <span>Ventas totales</span>
          <strong>2</strong>
        </div>
        <div className="data-row">
          <span>Dinero total de ventas</span>
          <span className="text-green">€ 76.000</span>
        </div>
        <div className="data-row">
          <span>Gastos totales</span>
          <strong>1</strong>
        </div>
        <div className="data-row">
          <span>Dinero total de gastos</span>
          <span className="text-red">€ 300.000</span>
        </div>
        
        <hr style={{margin: '20px 0', border: '1px solid #eee'}} />

        <div className="data-row">
          <strong>Balance del día</strong>
          <strong className="text-red">€ 224.000</strong>
        </div>
        <div className="data-row">
          <strong>Dinero caja para apertura</strong>
          <strong className="text-green">€ 100.000</strong>
        </div>

        <div className="floating-actions">
           <Link to="/dashboard/reportes" className="btn-action" style={{ display: 'inline-flex', alignItems: 'center', gap: '8px' }}>
             <BarChart3 size={18} /> Visualizar reportes
           </Link>
        </div>
      </div>
    </div>
  );
}