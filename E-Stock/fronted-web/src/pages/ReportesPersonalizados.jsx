import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Trash2, Edit2, X, Check, Plus } from 'lucide-react';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

export default function ReportesPersonalizados() {
  const navigate = useNavigate();
  
  const obtenerFechaLocal = (offsetDays = 0) => {
    const ahora = new Date();
    ahora.setDate(ahora.getDate() + offsetDays);
    const anio = ahora.getFullYear();
    const mes = String(ahora.getMonth() + 1).padStart(2, '0');
    const dia = String(ahora.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  };

  const [formData, setFormData] = useState({
    fechaInicio: obtenerFechaLocal(-7),
    fechaFin: obtenerFechaLocal(),
    periodicidad: "Diario",
    departamento: "Ventas",
  });

  const [busqueda, setBusqueda] = useState("");
  const [variables, setVariables] = useState([{ id: 1, nombre: "Precio total" }]);

  function handleChange(e) {
    const { id, value } = e.target;
    setFormData({ ...formData, [id]: value });
  }

  function agregarVariable() {
    if (!busqueda.trim()) return;
    setVariables([...variables, { id: Date.now(), nombre: busqueda }]);
    setBusqueda("");
  }

  function eliminarVariable(id) {
    setVariables(variables.filter(v => v.id !== id));
  }

  const generarReportePDF = () => {
    if (variables.length === 0) {
      alert("Por favor, añade al menos una variable para el reporte.");
      return;
    }

    try {
      const doc = new jsPDF();
      
      // Título
      doc.setFontSize(22);
      doc.setTextColor(44, 62, 80);
      doc.text("Reporte Personalizado E-Stock", 14, 22);
      
      
      doc.setFontSize(11);
      doc.setTextColor(127, 140, 141);
      doc.text(`Departamento: ${formData.departamento}`, 14, 32);
      doc.text(`Rango: ${formData.fechaInicio} al ${formData.fechaFin}`, 14, 38);
      doc.text(`Periodicidad: ${formData.periodicidad}`, 14, 44);
      doc.text(`Fecha de generación: ${new Date().toLocaleDateString()}`, 14, 50);

      

      const start = new Date(formData.fechaInicio);
      const end = new Date(formData.fechaFin);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

      const rows = [];
      const columns = ["Fecha", ...variables.map(v => v.nombre)];

      let currentDate = new Date(start);
      const steps = formData.periodicidad === "Diario" ? 1 : formData.periodicidad === "Semanal" ? 7 : 30;
      
      for (let i = 0; i < Math.min(diffDays, 31 * steps); i += steps) {
        const dateStr = currentDate.toISOString().split('T')[0];
        const row = [dateStr];
        
        variables.forEach(v => {
          const name = v.nombre.toLowerCase();
          if (name.includes("precio") || name.includes("total") || name.includes("dinero") || name.includes("ingreso") || name.includes("venta")) {
            row.push(`€ ${(Math.random() * 500 + 50).toFixed(2)}`);
          } else if (name.includes("cantidad") || name.includes("stock") || name.includes("producto") || name.includes("unidad")) {
            row.push(Math.floor(Math.random() * 30 + 1).toString());
          } else {
            row.push(Math.floor(Math.random() * 100).toString());
          }
        });
        
        rows.push(row);
        currentDate.setDate(currentDate.getDate() + steps);
        if (currentDate > end) break;
      }

      autoTable(doc, {
        startY: 58,
        head: [columns],
        body: rows,
        theme: 'striped',
        headStyles: { fillColor: [44, 62, 80] },
        styles: { fontSize: 10, cellPadding: 6 }
      });

      doc.save(`Reporte_${formData.departamento}_${formData.periodicidad}.pdf`);
      alert("¡Reporte generado con éxito!");
    } catch (error) {
      console.error(error);
      alert("Hubo un error al generar el reporte.");
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    generarReportePDF();
  };

  return (
    <div className="card">
      <h2 className="page-title">Reportes personalizados</h2>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Fecha de inicio</label>
          <input 
            id="fechaInicio" 
            type="date" 
            className="form-input" 
            value={formData.fechaInicio} 
            onChange={handleChange} 
          />
        </div>

        <div className="form-group">
          <label>Fecha de fin</label>
          <input 
            id="fechaFin" 
            type="date" 
            className="form-input" 
            value={formData.fechaFin} 
            onChange={handleChange} 
          />
        </div>

        <div className="form-group">
          <label>Periodicidad del informe</label>
          <select id="periodicidad" className="form-input" value={formData.periodicidad} onChange={handleChange}>
            <option value="Diario">Diario</option>
            <option value="Semanal">Semanal</option>
            <option value="Mensual">Mensual</option>
          </select>
        </div>

        <div className="form-group">
          <label>Departamento del informe</label>
          <select id="departamento" className="form-input" value={formData.departamento} onChange={handleChange}>
            <option value="Ventas">Ventas</option>
            <option value="Inventario">Inventario</option>
          </select>
        </div>

        <div className="form-group">
          <label>Variables a tener en cuenta</label>
          {/* Contenedor de búsqueda */}
          <div className="form-actions" style={{marginTop: 0, justifyContent: 'flex-start', gap: '10px'}}>
             <input 
               type="text" 
               className="form-input" 
               placeholder="Buscar variable" 
               value={busqueda}
               onChange={(e) => setBusqueda(e.target.value)}
             />
              <button type="button" onClick={agregarVariable} className="btn-primary-black" style={{padding: '10px 20px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                <Plus size={16} />
              </button>
          </div>

       
          {variables.map(v => (
            <div key={v.id} className="variable-tag" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: '#f1f5f9', padding: '8px 12px', borderRadius: '6px', marginBottom: '8px'}}>
              <span>{v.nombre}</span>
              <div style={{display: 'flex', gap: '10px'}}>
                <span style={{cursor: 'pointer', display: 'flex', alignItems: 'center'}} className="text-blue">
                  <Edit2 size={16} />
                </span>
                <span style={{cursor: 'pointer', display: 'flex', alignItems: 'center'}} className="text-red" onClick={() => eliminarVariable(v.id)}>
                  <Trash2 size={16} />
                </span>
              </div>
            </div>
          ))}
        </div>

        <div className="form-actions" style={{display: 'flex', gap: '10px', marginTop: '20px'}}>
          <button type="button" onClick={() => navigate(-1)} className="btn-action" style={{display: 'flex', alignItems: 'center', gap: '5px'}}>
            <X size={16} /> Cancelar
          </button>
          <button type="submit" className="btn-action btn-primary-black" style={{display: 'flex', alignItems: 'center', gap: '5px'}}>
            <Check size={16} /> Generar reporte
          </button>
        </div>
      </form>
    </div>
  );
}