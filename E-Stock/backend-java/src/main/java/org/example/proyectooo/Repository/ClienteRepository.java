package org.example.proyectooo.Repository;

import org.example.proyectooo.Domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {

    Cliente findByNombre(String nombre);
}
