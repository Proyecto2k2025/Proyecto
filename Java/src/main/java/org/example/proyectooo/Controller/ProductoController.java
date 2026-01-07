package org.example.proyectooo.Controller;

import org.example.proyectooo.Domain.Producto;
import org.example.proyectooo.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET: Obtener todo el inventario (con filtro opcional por categoría)
    // Vista: Inventario actual (image_d2ba8a.png)
    @GetMapping
    public List<Producto> obtenerInventario(@RequestParam(required = false) String categoria) {
        // El Service ya usa JOIN FETCH para cargar las variantes de manera eficiente
        return productoService.obtenerInventario(categoria);
    }

    // POST/PUT: Registrar o actualizar un producto (Ingreso de productos)
    // Vista: Ingreso de productos (image_d2bdb1.png)
    @PostMapping
    public ResponseEntity<Producto> registrarOActualizarProducto(@RequestBody Producto producto) {
        try {
            Producto productoGuardado = productoService.registrarOActualizarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE: Solo accesible para administradores (en una configuración real de Spring Security)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.eliminarProducto(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}