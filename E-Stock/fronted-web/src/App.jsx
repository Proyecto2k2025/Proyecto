import { Routes, Route } from "react-router-dom";
import DashboardLayout from "./components/DashboardLayout";
import Inventario from "./pages/Inventario";
import IngresoProducto from "./pages/IngresoProducto";
import Facturacion from "./pages/Facturacion";
import Reportes from "./pages/Reportes";
import ReportesPersonalizados from "./pages/ReportesPersonalizados";
import Login from "./pages/Login";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route element={<DashboardLayout />}>
        <Route path="/" element={<h2 className="text-center mt-10">Bienvenido a E-Stock</h2>} />
        <Route path="/inventario" element={<Inventario />} />
        <Route path="/ingreso-producto" element={<IngresoProducto />} />
        <Route path="/facturacion" element={<Facturacion />} />
        <Route path="/reportes" element={<Reportes />} />
        <Route path="/reportes-personalizados" element={<ReportesPersonalizados />} />
      </Route>
    </Routes>
  );
}

export default App;