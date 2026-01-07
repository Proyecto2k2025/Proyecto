package org.example.proyectooo.Controller;


import org.example.proyectooo.Domain.Pedido;
import org.example.proyectooo.Service.PedidoService;
import org.example.proyectooo.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ReporteService reporteService;

    @Autowired
    public PedidoController(PedidoService pedidoService, ReporteService reporteService) {
        this.pedidoService = pedidoService;
        this.reporteService = reporteService;
    }

    // POST: Crear una nueva factura/pedido (desde el punto de venta)
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.crearFacturaYActualizarStock(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Stock Insuficiente
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el pedido.");
        }
    }

    // GET: Listar pedidos por fecha (Vista Facturación: Captura de pantalla 2025-11-22 221751.png)
    @GetMapping("/fecha/{date}")
    public List<Pedido> getPedidosPorFecha(@PathVariable("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString); // "YYYY-MM-DD"
        return pedidoService.obtenerPedidosPorFecha(date);
    }

    // GET: Resumen diario (Vista Resumen: image_d2baab.png)
    @GetMapping("/resumen/{date}")
    public Map<String, Object> getResumenDiario(@PathVariable("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return reporteService.getDailySummary(date);
    }
}
