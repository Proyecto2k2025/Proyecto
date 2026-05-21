import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getResumenDiario, getAnnualSalesData } from '../services/reporteService';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { TrendingUp, Euro, Package, CreditCard, Sliders } from 'lucide-react';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

export default function Reportes() {
  const navigate = useNavigate();
  const [fechaSeleccionada, setFechaSeleccionada] = useState(new Date().toISOString().split('T')[0]);
  const [resumen, setResumen] = useState({
    ventasTotales: 0,
    dineroTotalVentas: 0,
    gastosTotales: 0,
    dineroTotalGastos: 0,
    balanceDia: 0
  });
  
  const [datosAnuales, setDatosAnuales] = useState([]);
  const [cargando, setCargando] = useState(true);

  const cargarDatos = async () => {
    setCargando(true);
    try {
        const [resResumen, resAnual] = await Promise.all([
            getResumenDiario(fechaSeleccionada),
            getAnnualSalesData()
        ]);
        setResumen(resResumen);
        setDatosAnuales(resAnual);
    } catch (error) {
        console.error("Error cargando dashboard:", error);
    } finally {
        setCargando(false);
    }
  };

  useEffect(() => {
    cargarDatos();
  }, [fechaSeleccionada]);

  if (cargando) {
      return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%', padding: '50px'}}>
            <h2>Cargando Dashboard... </h2>
        </div>
      );
  }

  const generarPDF = () => {
    try {
      const doc = new jsPDF();
      const fecha = new Date().toLocaleDateString();
      
      
      doc.setFontSize(20);
      doc.setTextColor(44, 62, 80);
      doc.text("Resumen Diario de Caja", 14, 22);
      
      doc.setFontSize(11);
      doc.setTextColor(127, 140, 141);
      doc.text(`Fecha de generacion: ${fecha}`, 14, 30);
        
     
      autoTable(doc, {
        startY: 40,
        head: [['Concepto', 'Valor']],
        body: [
          ['Ventas Realizadas Hoy', `${resumen.ventasTotales}`],
          ['Ingresos Totales', `€ ${resumen.dineroTotalVentas.toFixed(2)}`],
          ['Gastos Registrados', `€ ${resumen.dineroTotalGastos.toFixed(2)}`],
          ['BALANCE FINAL', `€ ${resumen.balanceDia.toFixed(2)}`]
        ],
        theme: 'striped',
        headStyles: { fillColor: [52, 152, 219] },
        styles: { fontSize: 12, cellPadding: 8 }
      });
      
      doc.setFontSize(10);
      doc.setTextColor(150, 150, 150);
      
      doc.text("Documento generado automaticamente por el sistema E-Stock.", 14, doc.lastAutoTable.finalY + 20);
      
     
      doc.save(`Resumen_Caja_${fecha.replace(/\//g, '-')}.pdf`);
    } catch (e) {
      console.error("Error al generar PDF:", e);
      alert("Hubo un error al generar el PDF. Verifica la consola.");
    }
  };

  return (
    <div>
      <div className="page-header-card" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '15px'}}>
        <div>
            <h1 className="page-title" style={{margin: 0}}>Dashboard de Reportes</h1>
            <p style={{color: '#7f8c8d', margin: '5px 0 0 0'}}>Resumen de la tienda y estado financiero</p>
        </div>
        <div style={{display: 'flex', gap: '15px', alignItems: 'center'}}>
            <div>
                <label style={{fontSize: '0.9em', color: '#7f8c8d', marginRight: '8px', fontWeight: 'bold'}}>Ver resumen del día:</label>
                <input 
                    type="date" 
                    value={fechaSeleccionada}
                    onChange={(e) => setFechaSeleccionada(e.target.value)}
                    style={{padding: '8px 12px', border: '1px solid #ddd', borderRadius: '5px', outline: 'none'}}
                />
            </div>
            <button onClick={cargarDatos} className="btn" style={{background: '#3498db', color: 'white'}}>
                🔄 Actualizar Datos
            </button>
        </div>
      </div>

      {/* Tarjetas de Resumen */}
      <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '20px', marginBottom: '30px'}}>
          
          <div className="card" style={{padding: '20px', display: 'flex', alignItems: 'center', gap: '15px'}}>
              <div style={{background: '#e0f2fe', padding: '15px', borderRadius: '50%'}}>
                  <Package size={32} color="#0284c7" />
              </div>
              <div>
                  <p style={{margin: 0, color: '#7f8c8d', fontSize: '0.9em', fontWeight: 'bold'}}>Ventas Hoy</p>
                  <h3 style={{margin: '5px 0 0 0', fontSize: '1.8em'}}>{resumen.ventasTotales}</h3>
              </div>
          </div>

          <div className="card" style={{padding: '20px', display: 'flex', alignItems: 'center', gap: '15px'}}>
              <div style={{background: '#dcfce7', padding: '15px', borderRadius: '50%'}}>
                  <Euro size={32} color="#16a34a" />
              </div>
              <div>
                  <p style={{margin: 0, color: '#7f8c8d', fontSize: '0.9em', fontWeight: 'bold'}}>Ingresos Diarios</p>
                  <h3 style={{margin: '5px 0 0 0', fontSize: '1.8em', color: '#16a34a'}}>€{resumen.dineroTotalVentas.toFixed(2)}</h3>
              </div>
          </div>

          <div className="card" style={{padding: '20px', display: 'flex', alignItems: 'center', gap: '15px'}}>
              <div style={{background: '#fee2e2', padding: '15px', borderRadius: '50%'}}>
                  <CreditCard size={32} color="#dc2626" />
              </div>
              <div>
                  <p style={{margin: 0, color: '#7f8c8d', fontSize: '0.9em', fontWeight: 'bold'}}>Gastos (Simulados)</p>
                  <h3 style={{margin: '5px 0 0 0', fontSize: '1.8em', color: '#dc2626'}}>€{resumen.dineroTotalGastos.toFixed(2)}</h3>
              </div>
          </div>

          <div className="card" style={{padding: '20px', display: 'flex', alignItems: 'center', gap: '15px', border: '2px solid #f1c40f'}}>
              <div style={{background: '#fef9c3', padding: '15px', borderRadius: '50%'}}>
                  <TrendingUp size={32} color="#ca8a04" />
              </div>
              <div>
                  <p style={{margin: 0, color: '#7f8c8d', fontSize: '0.9em', fontWeight: 'bold'}}>Balance del Día</p>
                  <h3 style={{margin: '5px 0 0 0', fontSize: '1.8em', color: resumen.balanceDia >= 0 ? '#16a34a' : '#dc2626'}}>
                      €{resumen.balanceDia.toFixed(2)}
                  </h3>
              </div>
          </div>

      </div>

      {/* Gráficos */}
      <div style={{display: 'flex', gap: '20px', flexWrap: 'wrap'}}>
         
         <div className="card" style={{flex: 2, minWidth: '400px', padding: '20px'}}>
            <h3 style={{marginTop: 0, borderBottom: '1px solid #eee', paddingBottom: '10px'}}>📈 Evolución de Ventas (Año Actual)</h3>
            
            {datosAnuales.length > 0 ? (
                <div style={{ height: '350px', width: '100%', marginTop: '20px' }}>
                    <ResponsiveContainer width="100%" height="100%">
                        <BarChart data={datosAnuales} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                            <CartesianGrid strokeDasharray="3 3" vertical={false} />
                            <XAxis dataKey="mes" />
                            <YAxis tickFormatter={(value) => `€${value}`} />
                            <Tooltip formatter={(value) => [`€${value}`, "Ventas"]} labelStyle={{color: 'black'}} />
                            <Bar dataKey="ventas" fill="#3498db" radius={[4, 4, 0, 0]} />
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            ) : (
                <div style={{ height: '350px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#999' }}>
                    <p>No hay datos de ventas registrados este año aún.</p>
                </div>
            )}
         </div>

         <div className="card" style={{flex: 1, minWidth: '300px', padding: '20px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between'}}>
             <div>
                <h3 style={{marginTop: 0, borderBottom: '1px solid #eee', paddingBottom: '10px'}}>🛠️ Exportar Datos</h3>
                <p style={{color: '#666', lineHeight: '1.5'}}>Genera un documento PDF limpio con el resumen del cierre de caja del día de hoy.</p>
             </div>
             
             <div style={{display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '20px'}}>
                <button onClick={generarPDF} className="btn" style={{background: '#e74c3c', color: 'white', padding: '15px', width: '100%', fontSize: '1.1em', fontWeight: 'bold', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px'}}>
                    <span>📄</span> Descargar PDF Resumen Diario
                </button>
                <button onClick={() => navigate('/reportes-personalizados')} className="btn" style={{background: '#34495e', color: 'white', padding: '15px', width: '100%', fontSize: '1.1em', fontWeight: 'bold', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px'}}>
                    <Sliders size={18} /> Reportes Personalizados
                </button>
             </div>
         </div>
      </div>
    </div>
  );
}