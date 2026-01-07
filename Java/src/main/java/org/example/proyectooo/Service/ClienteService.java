package org.example.proyectooo.Service;

import org.example.proyectooo.Domain.Cliente;
import org.example.proyectooo.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        // Spring inyecta la instancia que Spring Data JPA creó automáticamente
        this.clienteRepository = clienteRepository;
    }

    /**
     * Guarda o actualiza un cliente.
     * @Transactional maneja el inicio y el commit/rollback de la transacción.
     */
    @Transactional
    public Cliente save(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }

        // Delegamos el guardado/actualización a Spring Data JPA
        return clienteRepository.save(cliente);
    }

    /**
     * Busca un cliente por ID.
     * La lectura simple no requiere @Transactional si open-in-view está activo, pero es buena práctica usarlo
     * para asegurar el contexto transaccional en el Service.
     */
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        // findById devuelve un Optional<T> en Spring Data JPA
        return clienteRepository.findById(id).orElse(null);
    }

    /**
     * Verifica la existencia de un cliente por ID.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        // findById ya está disponible en JpaRepository
        return clienteRepository.existsById(id);
    }

    /**
     * Lista todos los clientes.
     */
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // findAll también está disponible en JpaRepository
        return clienteRepository.findAll();
    }
}