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

        this.clienteRepository = clienteRepository;
    }

    /**
     * Guarda o actualiza un cliente.
     */
    @Transactional
    public Cliente save(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }


        return clienteRepository.save(cliente);
    }

    /**
     * Busca un cliente por ID.
     */
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {

        return clienteRepository.findById(id).orElse(null);
    }

    /**
     * Verifica la existencia de un cliente por ID.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {

        return clienteRepository.existsById(id);
    }

    /**
     * Lista todos los clientes.
     */
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {

        return clienteRepository.findAll();
    }
}