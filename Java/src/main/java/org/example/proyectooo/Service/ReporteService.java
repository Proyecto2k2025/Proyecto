package org.example.proyectooo.Service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery; // Importar NativeQuery para tipado
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para transacciones
import jakarta.persistence.EntityManagerFactory; // Reemplazar HibernateUtil

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ReporteService {


    private final SessionFactory ss;

    @Autowired
    public ReporteService(EntityManagerFactory factory) {
        // Obtenemos la SessionFactory subyacente de Hibernate
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("Factory is not a hibernate factory");
        }
        this.ss = factory.unwrap(SessionFactory.class);
    }


    /**
     * Resumen del Día (Utilizado en la vista "Resumen 14 de noviembre de 2023")
     * Calcula la suma total de ventas y el conteo de transacciones.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getDailySummary(LocalDate date) {
        // Nota: COALESCE se usa para que SUM devuelva 0.0 en lugar de NULL
        String sql = """
            SELECT 
                COALESCE(SUM(CASE WHEN p.estado = 'ENTREGADO' THEN p.total ELSE 0 END), 0) AS dineroVentas,
                COUNT(CASE WHEN p.estado = 'ENTREGADO' THEN 1 ELSE NULL END) AS numVentas
            FROM pedidos p
            WHERE DATE(p.fecha_pedido) = :fecha
        """;

        try (Session session = ss.getCurrentSession()) {

            // Usamos NativeQuery para mayor seguridad de tipos
            NativeQuery<Object[]> query = session.createNativeQuery(sql, Object[].class);
            query.setParameter("fecha", date);
            query.addScalar("dineroVentas", StandardBasicTypes.DOUBLE);
            query.addScalar("numVentas", StandardBasicTypes.LONG);

            Object[] result = query.uniqueResult();

            Map<String, Object> summary = new HashMap<>();

            // Mapeo manual y simulación de datos (se mantiene tu lógica)
            Double totalVentas = (result != null && result[0] != null) ? (Double) result[0] : 0.0;
            // Aquí iría una consulta real a la tabla de Gastos, pero mantenemos la simulación:
            Double totalGastosSimulado = 300.00;

            summary.put("ventasTotales", (result != null && result[1] != null) ? (Long) result[1] : 0L);
            summary.put("dineroTotalVentas", totalVentas);
            summary.put("gastosTotales", 1L);
            summary.put("dineroTotalGastos", totalGastosSimulado);
            summary.put("balanceDia", totalVentas - totalGastosSimulado);


            return summary;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el resumen diario.", e);
        }
    }


    /**
     * Datos de Ventas Anuales (Utilizado en la gráfica "Ventas año 2023")
     */
    @Transactional(readOnly = true)
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
            // Consulta nativa sin mapeo de clases
            List<Object[]> results = session.createNativeQuery(sql).list();


            return results.stream().map(row -> {
                Map<String, Object> data = new HashMap<>();

                data.put("mes", row[0]);
                data.put("ventas", row[1]);
                return data;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar datos anuales.", e);
        }
    }

    /**
     * Reporte Personalizado (Utilizado en la vista "Reportes personalizados")
     * Simulación de la lógica de construcción de consulta dinámica.
     */
    public List<Map<String, Object>> generateCustomReport(Map<String, Object> filters) {

        System.out.println("Procesando consulta personalizada con filtros: " + filters.toString());

        return List.of(Map.of("message", "Reporte personalizado OK", "filtros_usados", filters));
    }
}