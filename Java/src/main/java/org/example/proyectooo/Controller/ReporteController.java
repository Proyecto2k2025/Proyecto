package org.example.proyectooo.Controller;


import org.example.proyectooo.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // GET: Muestra la gráfica de Ventas Anuales (Ventas año 2023)
    // Vista: Reportes (image_d2ba6b.png)
    @GetMapping("/ventas/anual")
    public ResponseEntity<List<Map<String, Object>>> getAnnualSalesData() {
        return ResponseEntity.ok(reporteService.getAnnualSalesData());
    }

    // POST: Genera reportes personalizados (Reportes personalizados)
    // Vista: Reportes personalizados (Captura de pantalla 2025-11-22 221833.png)
    @PostMapping("/custom")
    public ResponseEntity<List<Map<String, Object>>> generateCustomReport(@RequestBody Map<String, Object> filters) {
        return ResponseEntity.ok(reporteService.generateCustomReport(filters));
    }
}