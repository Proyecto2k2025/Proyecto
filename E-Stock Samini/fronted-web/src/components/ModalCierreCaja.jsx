import { useState, useEffect } from "react";
import { getResumenDiario } from "../services/pedidoService";

export default function ModalCierreCaja({ onClose }) {
  const [resumen, setResumen] = useState(null);
  const fechaHoy = new Date().toISOString().split('T')[0];

  useEffect(() => {
   
    getResumenDiario(fechaHoy).then(setResumen);
  }, []);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded shadow-lg w-80 text-center">
        <h2 className="text-xl font-bold mb-4">Cierre de Caja</h2>
        <p className="text-gray-500 mb-4">Fecha: {fechaHoy}</p>

        {resumen ? (
          <div className="text-left space-y-2 mb-6">
            <p>Ventas Totales: <b className="text-green-600">€{resumen.totalVentas || 0}</b></p>
            <p>Gastos Totales: <b className="text-red-600">€{resumen.totalGastos || 0}</b></p>
            <hr/>
            <p className="text-xl font-bold text-center mt-2">
                Balance: €{(resumen.totalVentas || 0) - (resumen.totalGastos || 0)}
            </p>
          </div>
        ) : (
          <p>Calculando...</p>
        )}

        <button onClick={onClose} className="bg-blue-600 text-white px-6 py-2 rounded">
          Aceptar
        </button>
      </div>
    </div>
  );
}