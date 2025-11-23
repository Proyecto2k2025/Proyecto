package org.example.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ReporteService {
    private SessionFactory ss;

    public ReporteService( ) {
        this.ss = org.example.config.HibernateUtil.getSessionFactory();

    }


    /**
     * Resumen del Día
     * Calcula la suma total de ventas y el conteo de transacciones.
     */
    public Map<String, Object> getDailySummary(LocalDate date) {
        // SQL Nativo para mayor control sobre SUM y COUNT
        String sql = """
            SELECT 
                COALESCE(SUM(CASE WHEN p.estado = 'ENTREGADO' THEN p.total ELSE 0 END), 0) AS dineroVentas,
                COUNT(CASE WHEN p.estado = 'ENTREGADO' THEN 1 ELSE NULL END) AS numVentas
            FROM pedidos p
            WHERE DATE(p.fecha_pedido) = :fecha
        """;

        try (Session session = ss.getCurrentSession()) {
            // Ejecutar consulta y mapear tipos
            Object[] result = (Object[]) session.createNativeQuery(sql)
                    .setParameter("fecha", date)
                    .addScalar("dineroVentas", StandardBasicTypes.DOUBLE) // Asumiendo Double para total
                    .addScalar("numVentas", StandardBasicTypes.LONG)
                    .uniqueResult();

            Map<String, Object> summary = new HashMap<>();

            // Mapeo manual y simulación de datos
            Double totalVentas = (result != null && result[0] != null) ? (Double) result[0] : 0.0;
            Double totalGastosSimulado = 300.00;

            summary.put("ventasTotales", (result != null && result[1] != null) ? (Long) result[1] : 0L);
            summary.put("dineroTotalVentas", totalVentas);
            summary.put("gastosTotales", 1L); // Hardcodeado por falta de tabla Gastos
            summary.put("dineroTotalGastos", totalGastosSimulado);
            summary.put("balanceDia", totalVentas - totalGastosSimulado);

            return summary;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el resumen diario.", e);
        }
    }


    public List<Map<String, Object>> getAnnualSalesData() {

        String sql = """
            SELECT 
                MONTH(fecha_pedido) AS mes, 
                SUM(total) AS total_ventas
            FROM pedidos 
            WHERE YEAR(fecha_pedido) = YEAR(CURRENT_DATE()) AND estado = 'ENTREGADO'
            GROUP BY mes 
            ORDER BY mes
        """;

        try (Session session = ss.getCurrentSession()) {
            List<Object[]> results = session.createNativeQuery(sql).list();


            return results.stream().map(row -> {
                Map<String, Object> data = new HashMap<>();
                data.put("mes", row[0]);
                data.put("ventas", row[1]);
                return data;
            }).toList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar datos anuales.", e);
        }
    }

    /**
     *Reporte Personalizado
     * Simulación de la lógica de construcción de consulta dinámica.
     */
    public List<Map<String, Object>> generateCustomReport(Map<String, Object> filters) {
        System.out.println("Procesando consulta personalizada con filtros: " + filters.toString());

        return List.of(Map.of("message", "Reporte personalizado OK", "filtros_usados", filters));
    }
}
