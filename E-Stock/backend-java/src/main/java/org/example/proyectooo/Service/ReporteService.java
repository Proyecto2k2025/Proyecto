package org.example.proyectooo.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Map<String, Object> getDailySummary(LocalDate date) {
        String sql = """
            SELECT 
                COALESCE(SUM(CASE WHEN p.estado = 'ENTREGADO' THEN p.total ELSE 0 END), 0) AS dineroVentas,
                COUNT(CASE WHEN p.estado = 'ENTREGADO' THEN 1 ELSE NULL END) AS numVentas
            FROM pedidos p
            WHERE DATE(p.fecha_pedido) = :fecha
        """;

        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("fecha", date)
                    .getSingleResult();

            Map<String, Object> summary = new HashMap<>();

            Double totalVentas = (result != null && result[0] != null) ? ((Number) result[0]).doubleValue() : 0.0;
            Long numVentas = (result != null && result[1] != null) ? ((Number) result[1]).longValue() : 0L;
            Double totalGastosSimulado = 100.00;

            summary.put("ventasTotales", numVentas);
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

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();

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

    public List<Map<String, Object>> generateCustomReport(Map<String, Object> filters) {
        return List.of(Map.of("message", "Reporte personalizado OK", "filtros_usados", filters));
    }
}
