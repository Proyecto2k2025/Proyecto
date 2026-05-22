package org.example.proyectooo.Service;

import org.example.proyectooo.Domain.Producto;
import org.example.proyectooo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    public List<Producto> obtenerInventario(String categoria) {
        if (categoria != null && !categoria.trim().isEmpty()) {
            return productoRepository.findByCategoriaWithVariantes(categoria);
        } else {
            return productoRepository.findAllWithVariantes();
        }
    }

    @Transactional
    public Producto registrarOActualizarProducto(Producto producto) {

        if (producto.getVariantes() != null) {
            producto.getVariantes().forEach(v -> v.setProducto(producto));
        }

        return productoRepository.save(producto);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean eliminarProducto(Long id) {

        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}