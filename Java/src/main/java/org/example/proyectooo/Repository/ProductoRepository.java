package org.example.proyectooo.Repository;

import org.example.proyectooo.Domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {


    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.variantes")
    List<Producto> findAllWithVariantes();


    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.variantes WHERE p.categoria = ?1")
    List<Producto> findByCategoriaWithVariantes(String categoria);
}